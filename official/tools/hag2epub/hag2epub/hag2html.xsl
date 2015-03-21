<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2012-2013  Stephan Kreutzer

This file is part of Freie Bibel.

Freie Bibel is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3 or any later version,
as published by the Free Software Foundation.

Freie Bibel is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License 3 for more details.

You should have received a copy of the GNU General Public License
along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"/>
  <xsl:template match="XMLBIBLE">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head profile="http://dublincore.org/documents/dcq-html/">
        <title><xsl:value-of select="/XMLBIBLE/@biblename"/></title>

        <link rel="schema.DC" href="http://purl.org/dc/elements/1.1/"/>

        <meta name="DC.title" content="{/XMLBIBLE/INFORMATION/title}"/>
        <meta name="DC.creator" content="{/XMLBIBLE/INFORMATION/creator}"/>
        <meta name="DC.generator" content="hag2html.xsl of hag2epub (http://www.freie-bibel.de)"/>
        <meta name="DC.description" content="{/XMLBIBLE/INFORMATION/description}"/>
        <meta name="DC.publisher" content="{/XMLBIBLE/INFORMATION/publisher}"/>
        <meta name="DC.subject" content="{/XMLBIBLE/INFORMATION/subject}"/>
        <xsl:for-each select="/XMLBIBLE/INFORMATION/contributor">
          <meta name="DC.contributor" content="{.}"/>
        </xsl:for-each>
        <meta name="DC.date" content="{/XMLBIBLE/INFORMATION/date}"/>
        <meta name="DC.type" content="Text"/>
        <meta name="DC.format" content="application/xhtml+xml"/>
        <meta name="DC.identifier" content="{/XMLBIBLE/INFORMATION/identifier}"/>
        <meta name="DC.source" content="{/XMLBIBLE/INFORMATION/source}"/>
        <meta name="DC.language" content="{/XMLBIBLE/INFORMATION/language}"/>
        <meta name="DC.coverage" content="{/XMLBIBLE/INFORMATION/coverage}"/>
        <meta name="DC.rights" content="{/XMLBIBLE/INFORMATION/rights}"/>

        <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
      </head>
      <body>
        <div>
          <h1><xsl:value-of select="/XMLBIBLE/@biblename"/></h1>
          <ul id="metadata_information">
            <xsl:for-each select="INFORMATION/*">
              <li>
                <tt><xsl:value-of select="local-name()"/>: <xsl:value-of select="."/></tt>
              </li>
            </xsl:for-each>
          </ul>
          <xsl:apply-templates select="BIBLEBOOK"/>
        </div>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="BIBLEBOOK">
    <div id="id_{./@bnumber}">
      <xsl:choose>
        <xsl:when test="./CAPTION">
          <h2><xsl:value-of select="./CAPTION"/></h2>
        </xsl:when>
        <xsl:when test="./@bname">
          <h2><xsl:value-of select="./@bname"/></h2>
        </xsl:when>
        <xsl:when test="./@bsname">
          <h2><xsl:value-of select="./@bsname"/></h2>
        </xsl:when>
        <xsl:otherwise>
          <h2><xsl:value-of select="./@bnumber"/><xsl:text>. Buch</xsl:text></h2>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates select="CHAPTER"/>
    </div>
  </xsl:template>
  <xsl:template match="CHAPTER">
    <div id="id_{../@bnumber}_{./@cnumber}">
      <h3><xsl:text>Kapitel&#x20;</xsl:text><xsl:value-of select="@cnumber"/></h3>
      <div>
        <xsl:apply-templates select="VERSE | PARAGRAPH"/>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="PARAGRAPH">
    <p>
      <xsl:apply-templates select="VERSE"/>
    </p>
  </xsl:template>
  <xsl:template match="VERSE">
    <sup>
      <xsl:value-of select="@vnumber"/>
    </sup>
    <xsl:apply-templates/>
    <xsl:text>&#x20;</xsl:text>
  </xsl:template>
  <xsl:template match="VERSE//text()">
    <xsl:value-of select="."/>
  </xsl:template>
  <xsl:template match="STYLE">
    <xsl:choose>
      <xsl:when test="@fs='super'">
        &#x27E8;<xsl:value-of select="."/>&#x27E9;
      </xsl:when>
      <xsl:when test="@fs='emphasis'">
        <b><xsl:value-of select="."/></b>
      </xsl:when>
      <xsl:when test="@fs='italic'">
        <i><xsl:value-of select="."/></i>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="NOTE">
    <abbr title="{.}">*</abbr>
  </xsl:template>
</xsl:stylesheet>
