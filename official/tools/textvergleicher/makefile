# Copyright (C) 2011-2013  Stephan Kreutzer
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



PREFIX ?= /usr/local
BINDIR ?= ${PREFIX}/bin
.PHONY: textvergleicher install clean



textvergleicher: ConfigProcessor.class BibleProcessor.class HtmlProcessor.class textvergleicher.class

textvergleicher.class: textvergleicher.java
	@echo "  --  compiling textvergleicher..."
	javac textvergleicher.java

ConfigProcessor.class: ConfigProcessor.java
	@echo "  --  compiling config processor..."
	javac ConfigProcessor.java

BibleProcessor.class: BibleProcessor.java
	@echo "  --  compiling bible processor..."
	javac BibleProcessor.java

HtmlProcessor.class: HtmlProcessor.java
	@echo "  --  compiling html processor..."
	javac HtmlProcessor.java

${BINDIR}:
	@echo "  --  creating bin directory..."
	mkdir -p ${BINDIR}

install: textvergleicher ${BINDIR}
	@echo "  --  installing textvergleicher..."
	mkdir -p ${BINDIR}/textvergleicher
	cp ConfigProcessor.class ${BINDIR}/textvergleicher
	cp BibleProcessor.class ${BINDIR}/textvergleicher
	cp HtmlProcessor.class ${BINDIR}/textvergleicher
	cp textvergleicher.class ${BINDIR}/textvergleicher
	@echo "  --  creating textvergleich script..."
	touch ${BINDIR}/textvergleich
	@echo "#!/bin/sh" >> ${BINDIR}/textvergleich
	@printf "\x0A" >> ${BINDIR}/textvergleich
	@printf "java -cp ${BINDIR}/textvergleicher textvergleicher \x241" >> ${BINDIR}/textvergleich
	@printf "\x0A" >> ${BINDIR}/textvergleich

clean:
	@echo "  --  cleaning textvergleicher..."
	rm -f textvergleicher.class
	rm -f ConfigProcessor.class
	rm -f BibleProcessor.class
	rm -f HtmlProcessor.class
