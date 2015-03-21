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
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="UTF-8"/>
  <xsl:param name="Seitenbreite">210mm</xsl:param>
  <xsl:param name="Seitenhoehe">297mm</xsl:param>
  <xsl:param name="Rand-rechte-Seiten">20mm 20mm 20mm 20mm</xsl:param>
  <xsl:param name="Rand-linke-Seiten">20mm 20mm 20mm 20mm</xsl:param>
  <xsl:param name="Innenrand-rechte-Seiten">0mm 95mm 0mm 0mm</xsl:param>
  <xsl:param name="Innenrand-linke-Seiten">0mm 0mm 0mm 95mm</xsl:param>
  <!-- See http://xmlgraphics.apache.org/fop/trunk/fonts.html -->
  <xsl:param name="Schriftname">Old Standard TT</xsl:param>
  <xsl:param name="Schriftgroesse">8pt</xsl:param>
  <xsl:param name="Zeilenhoehe">9.6pt</xsl:param>

  <xsl:template match="/">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master page-height="{$Seitenhoehe}" page-width="{$Seitenbreite}" margin="{$Rand-rechte-Seiten}" master-name="Rechte Seite">
          <fo:region-body margin="{$Innenrand-rechte-Seiten}"/>
          <fo:region-before region-name="Kopf-rechte-Seiten" extent="5mm" display-align="before"/>
        </fo:simple-page-master>
        <fo:simple-page-master page-height="{$Seitenhoehe}" page-width="{$Seitenbreite}" margin="{$Rand-linke-Seiten}" master-name="Linke Seite">
          <fo:region-body margin="{$Innenrand-linke-Seiten}"/>
          <fo:region-before region-name="Kopf-linke-Seiten" extent="5mm" display-align="before"/>
        </fo:simple-page-master>
        <fo:page-sequence-master master-name="Inhalt-Seite">
          <fo:repeatable-page-master-alternatives>
            <fo:conditional-page-master-reference master-reference="Rechte Seite" odd-or-even="odd"/>
            <fo:conditional-page-master-reference master-reference="Linke Seite" odd-or-even="even"/>
          </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="Inhalt-Seite">
        <fo:static-content flow-name="Kopf-rechte-Seiten">
          <fo:block font-family="{$Schriftname}" font-size="{$Schriftgroesse}" line-height="{$Zeilenhoehe}" text-align="right">
            <fo:page-number/>
          </fo:block>
        </fo:static-content>
        <fo:static-content flow-name="Kopf-linke-Seiten">
          <fo:block font-family="{$Schriftname}" font-size="{$Schriftgroesse}" line-height="{$Zeilenhoehe}">
            <fo:page-number/>
          </fo:block>
        </fo:static-content>
        <fo:static-content flow-name="xsl-footnote-separator">
          <fo:block text-align-last="justify" space-before="1mm" space-after="1mm">
            <fo:leader leader-length="20mm" rule-thickness="0.5pt" leader-pattern="rule"/>
          </fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body">
          <!-- fo:block font-family="{$Schriftname}" font-size="{$Schriftgroesse}" line-height="{$Zeilenhoehe}" hyphenate="true" lang="de" -->
          <fo:block font-family="{$Schriftname}" font-size="{$Schriftgroesse}" line-height="{$Zeilenhoehe}">
            <xsl:apply-templates select="/XMLBIBLE/BIBLEBOOK"/>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>

  <xsl:template match="/XMLBIBLE/BIBLEBOOK/CAPTION">
    <fo:block font-size="{$Schriftgroesse} * 1.4" line-height="{$Schriftgroesse} * 1.3" font-weight="bold" text-align="center" space-after="6mm" space-after.precedence="1" keep-with-next.within-page="always">
      <xsl:value-of select="."/>
    </fo:block>
  </xsl:template>
  <xsl:template match="PARAGRAPH">
    <fo:block space-after="{$Zeilenhoehe}" text-align="justify">
      <xsl:apply-templates select="VERSE"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="VERSE">
    <xsl:apply-templates/>
    <xsl:text>&#x0020;</xsl:text>
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
        <fo:inline font-weight="bold">
          <xsl:value-of select="."/>
        </fo:inline>
      </xsl:when>
      <xsl:when test="@fs='italic'">
        <fo:inline font-style="italic">
          <xsl:value-of select="."/>
        </fo:inline>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="NOTE">
    <fo:footnote>
      <!--fo:inline baseline-shift="super" font-size="67%"-->
      <fo:inline baseline-shift="{$Schriftgroesse}*0.33" font-size="67%">
        <xsl:number level="any" count="NOTE" from="BIBLEBOOK" format="1"/>
      </fo:inline>
      <fo:footnote-body>
        <fo:list-block provisional-distance-between-starts="{$Zeilenhoehe} * 1.5" space-after="4pt" start-indent="0pt">
          <fo:list-item>
            <fo:list-item-label>
              <fo:block font-size="80%" text-indent="0pt">
                <xsl:number level="any" count="NOTE" from="BIBLEBOOK" format="1"/>
              </fo:block>
            </fo:list-item-label>
            <fo:list-item-body start-indent="body-start()">
              <fo:block font-size="80%">
                <xsl:apply-templates/>
              </fo:block>
            </fo:list-item-body>
          </fo:list-item>
        </fo:list-block>
      </fo:footnote-body>
    </fo:footnote>
  </xsl:template>

</xsl:stylesheet>
