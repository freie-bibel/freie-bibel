<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2012-2015  Stephan Kreutzer

This file is part of Free Scriptures.

Free Scriptures is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

Free Scriptures is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with Free Scriptures. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"/>
  <xsl:template match="/XMLBIBLE">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
      <xsl:comment> This file was generated by title.xsl of haggai2epub1 for Offene Bibel, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/free-scriptures/free-scriptures/ and http://www.free-scriptures.org). </xsl:comment>
      <head>
        <title>Titel</title>
        <meta name="generator" content="title.xsl of haggai2epub1 for Offene Bibel (http://www.free-scriptures.org)"/>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
      </head>
      <body>
        <div>
          <h1>
            <xsl:choose>
              <xsl:when test="/XMLBIBLE/INFORMATION/title">
                <xsl:value-of select="/XMLBIBLE/INFORMATION/title"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="/XMLBIBLE/@biblename"/>
              </xsl:otherwise>
            </xsl:choose>
          </h1>
          <xsl:if test="/XMLBIBLE/INFORMATION/rights">
            <p>
              <xsl:value-of select="/XMLBIBLE/INFORMATION/rights"/>
            </p>
          </xsl:if>
          <div id="id_books">
            <h2>Bibelbücher</h2>
            <ul>
              <xsl:for-each select="BIBLEBOOK">
                <li>
                  <a>
                    <xsl:attribute name="href"><xsl:number count="/XMLBIBLE/BIBLEBOOK"/><xsl:text>.xhtml</xsl:text></xsl:attribute>
                    <xsl:choose>
                      <xsl:when test="./CAPTION">
                        <xsl:value-of select="./CAPTION"/>
                      </xsl:when>
                      <xsl:when test="./@bname">
                        <xsl:value-of select="./@bname"/>
                      </xsl:when>
                      <xsl:when test="./@bsname">
                        <xsl:value-of select="./@bsname"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="./@bnumber"/><xsl:text>. Buch</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </a>
                </li>
              </xsl:for-each>
            </ul>
          </div>
          <p style="text-align: center;">
            <!--
              Path relative to $/official/tools/free-scriptures/tools/workflows/haggai2epub1/temp/components/,
              the location of the title.xhtml.
            -->
            Volltext der <a rel="copyright" href="./../../../../../../offene_bibel/license.xhtml">Lizenzbestimmungen</a>
          </p>
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>