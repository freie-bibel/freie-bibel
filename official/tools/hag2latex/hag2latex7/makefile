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



.PHONY: hag2latex7 clean



hag2latex7: ParallelProcessor.class hag2latex7.class

hag2latex7.class: hag2latex7.java
	@echo "  --  compiling hag2latex7..."
	javac hag2latex7.java

ParallelProcessor.class: ParallelProcessor.java
	@echo "  --  compiling parallel processor..."
	javac ParallelProcessor.java

clean:
	@echo "  --  cleaning hag2latex7..."
	rm -f hag2latex7.class
	rm -f ParallelProcessor.class

