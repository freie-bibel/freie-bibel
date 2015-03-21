#!/bin/bash
# Copyright (C) 2013  Stephan Kreutzer
#
# This file is part of Freie Bibel.
#
# Freie Bibel is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# Freie Bibel is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License 3 for more details.
#
# You should have received a copy of the GNU General Public License
# along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.



if [ $# -ne 1 ]; then
    printf "Usage:\n\tpdf2png.sh infile.pdf\n\n"
    exit 1
fi

if ! test -r "$1"; then
    printf "pdf2png.sh: '$1' isn't readable.\n"
    exit -1;
fi

pdftops $1 temp.ps

pages=$(pdfinfo $1 | grep Pages | cut -d ":" -f 2)

if [ $pages -eq 0 ]; then
    printf "pdf2png.sh: pdfinfo failed, trying pdftops/showpage...\n"
    pages=$(grep showpage temp.ps | wc -l)
fi

for (( page=1; page<=$pages; page++ ))
do
    printf "Processing page $page...\n"
    psselect -q -p$page temp.ps $page.ps
    # The -r parameter specifies the quality in DPI, common values are 300 and 600.
    gs -dBATCH -sOutputFile=$page.png -r600 -sDEVICE=png16m $page.ps > /dev/null << EOF

EOF
    rm $page.ps
done

rm temp.ps

