#!/bin/bash
# Copyright (C) 2013  Stephan Kreutzer
#
# This file is part of Freie Bibel.
#
# Freie Bibel is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# Freie Bibel is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.

# All "-dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89"
# refers to DIN A4. Change these settings for other page formats. Was needed
# because DIN A4 was considered 595.00x842.00 on some systems.



printf "interleave.sh  Copyright (C) 2013  Stephan Kreutzer\n"
printf "This program comes with ABSOLUTELY NO WARRANTY.\n"
printf "This is free software, and you are welcome to redistribute it\n"
printf "under certain conditions. See the GNU Affero General Public\n"
printf "License 3 or any later version for details.\n\n"

if [ $# -ne 4 ]; then
    printf "Usage:\n\tinterleave.sh infile.pdf blankpage.pdf style out.pdf\n\n"
    printf "style\t'l' for left pages blank, 'r' for right pages blank, 'c' for classical\n"
    printf "\tinterleaving (blank pages by page sheet, left/right alternating).\n\n"
    printf "Limited to DIN A4. For other formats, change the script.\n"
    exit 1
fi

if ! test -r "$1"; then
    printf "interleave.sh: $1 isn't readable.\n"
    exit -1;
fi

if ! test -r "$2"; then
    printf "interleave.sh: $2 isn't readable.\n"
    exit -2;
fi

printf "interleave.sh: Converting $1...\n\n"

pdftops $1 temp.ps
pdftops $2 blank.ps

pages=$(pdfinfo $1 | grep Pages | cut -d ":" -f 2)

if [ $pages -eq 0 ]; then
    printf "interleave.sh: pdfinfo failed, trying pdftops/showpage...\n"
    pages=$(grep showpage temp.ps | wc -l)
fi

blankpages=$(pdfinfo $2 | grep Pages | cut -d ":" -f 2)

if [ $blankpages -eq 0 ]; then
    printf "interleave.sh: pdfinfo failed, trying pdftops/showpage...\n"
    blankpages=$(grep showpage blank.ps | wc -l)
fi

if [ $blankpages -ne 1 ]; then
    printf "interleave.sh: $2 has a different page count than 1.\n"
    rm blank.ps
    rm temp.ps
    exit -3;
fi

if [ $3 == "l" ]; then
    filelist=""

    printf "interleave.sh: Preparing page "

    for (( page=1; page<=$pages; page++ ))
    do
        if [ $page -ne $pages ]; then
            printf "$page, "
        else
            printf "$page.\n\n"
        fi

        psselect -q -p$page temp.ps $page.ps
    done

    printf "interleave.sh: "

    for (( page=1; page<=$pages; page++ ))
    do
        if [ $page -eq 1 ]; then
            filelist="blank.ps blank.ps $page.ps"
            printf "$2, $2, page $page, "
        fi

        if [ $page -ne 1 ]; then
            filelist="$filelist blank.ps $page.ps"
            printf "$2, page $page, "
        fi

        if [ $page -eq $pages ]; then
            filelist="$filelist blank.ps"
            printf "$2.\n\n"
        fi
    done

    printf "interleave.sh: Interleaving...\n"

    gs -q -dNOPAUSE -dBATCH -dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89 -sDEVICE=ps2write -sOutputFile=$4.ps $filelist

    for (( page=1; page<=$pages; page++ ))
    do
        rm $page.ps
    done

    printf "interleave.sh: Converting to $4...\n\n"

    ps2pdf -dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89 $4.ps $4
    rm $4.ps

    rm blank.ps
    rm temp.ps
    exit 0;
fi

if [ $3 == "r" ]; then
    filelist=""

    printf "interleave.sh: Preparing page "

    for (( page=1; page<=$pages; page++ ))
    do
        if [ $page -ne $pages ]; then
            printf "$page, "
        else
            printf "$page.\n\n"
        fi

        psselect -q -p$page temp.ps $page.ps
    done

    printf "interleave.sh: "

    for (( page=1; page<=$pages; page++ ))
    do
        if [ $page -eq 1 ]; then
            filelist="blank.ps $page.ps blank.ps"
            printf "$2, page $page, $2, "
        fi

        if [ $page -ne 1 ]; then
            filelist="$filelist $page.ps blank.ps"
            printf "page $page, $2, "
        fi

        if [ $page -eq $pages ]; then
            filelist="$filelist blank.ps"
            printf "$2.\n\n"
        fi
    done

    printf "interleave.sh: Interleaving...\n"

    gs -q -dNOPAUSE -dBATCH -dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89 -sDEVICE=ps2write -sOutputFile=$4.ps $filelist

    for (( page=1; page<=$pages; page++ ))
    do
        rm $page.ps
    done

    printf "interleave.sh: Converting to $4...\n\n"

    ps2pdf -dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89 $4.ps $4
    rm $4.ps

    rm blank.ps
    rm temp.ps
    exit 0;
fi

if [ $3 == "c" ]; then

    if [ `echo "$pages % 4" | bc` -ne 0 ]; then
        printf "interleave.sh: a page count of $pages in $2 isn't allowed with style 'classic', "
        printf "it must be devidable by 4.\n"
        rm blank.ps
        rm temp.ps
        exit -4; 
    fi

    middle=`echo "$pages / 2" | bc`;

    filelist=""

    printf "interleave.sh: Preparing page "

    for (( page=1; page<=$pages; page += 2 ))
    do
        page2=`echo "$page + 1" | bc`;

        if [ $page2 -ne $pages ]; then
            printf "$page, $page2, "
        else
            printf "$page, $page2.\n\n"
        fi

        psselect -q -p$page temp.ps $page.ps
        psselect -q -p$page2 temp.ps $page2.ps
    done

    printf "interleave.sh: "

    for (( page=1; page<=$pages; page += 2 ))
    do
        page2=`echo "$page + 1" | bc`;

        if [ $page -eq 1 ]; then
            filelist="blank.ps blank.ps"
            printf "$2, $2, "
        fi

        if [ $page2 -eq $middle ]; then
            filelist="$filelist $page.ps $page2.ps blank.ps blank.ps blank.ps blank.ps"
            printf "page $page, page $page2, $2, $2, $2, $2, "
        else
            filelist="$filelist $page.ps $page2.ps blank.ps blank.ps"

            if [ $page2 -ne $pages ]; then
                printf "page $page, page $page2, $2, $2, "
            else
                printf "page $page, page $page2, $2, $2.\n\n"
            fi
        fi
    done

    printf "interleave.sh: Interleaving...\n"

    gs -q -dNOPAUSE -dBATCH -dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89 -sDEVICE=ps2write -sOutputFile=$4.ps $filelist

    for (( page=1; page<=$pages; page++ ))
    do
        rm $page.ps
    done

    printf "interleave.sh: Converting to $4...\n\n"

    ps2pdf -dFIXEDMEDIA -dDEVICEWIDTHPOINTS=595.28 -dDEVICEHEIGHTPOINTS=841.89 $4.ps $4
    rm $4.ps

    rm blank.ps
    rm temp.ps
    exit 0;
fi

printf "interleave.sh: unknown option $3 for parameter 'style'.\n"

rm blank.ps
rm temp.ps

