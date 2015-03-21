<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2012-2013  Stephan Kreutzer

This file is part of Freie Bibel.

Freie Bibel is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

Freie Bibel is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
  <xsl:template match="XMLBIBLE">
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>&#xA;</xsl:text>
    <xsl:comment> This file was generated by hag2infohtml.xsl of hag2epub2 (http://www.freie-bibel.de). </xsl:comment>
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops">
      <head>
        <title>Information</title>

        <link rel="schema.DC" href="http://purl.org/dc/elements/1.1/"/>

        <meta name="DC.generator" content="hag2infohtml.xsl of hag2epub2 (http://www.freie-bibel.de)"/>
        <meta name="DC.type" content="Text"/>
        <meta name="DC.format" content="application/xhtml+xml"/>
      </head>
      <body>
        <div>
          <h1>Information</h1>
          <ul id="id_information">
            <xsl:for-each select="INFORMATION/*">
              <li>
                <span style="font-family: monospace;"><xsl:value-of select="local-name()"/>: <xsl:value-of select="."/></span>
              </li>
            </xsl:for-each>
          </ul>
          <div>
            <h1>Bibelbücher</h1>
            <ul id="id_buchuebersicht">
              <xsl:for-each select="BIBLEBOOK">
                <li>
                  <a href="{./@bnumber}.xhtml">
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
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
