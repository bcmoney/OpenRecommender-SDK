<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>
<xsl:template match="/">
  <html>
  <body>
    <table border="1">
      <tr bgcolor="#9acd32">
        <th align="left"><h2>OpenRecommender - Recommendations</h2></th>
      </tr>
      <xsl:for-each select="recommendations/recommendation">
      <tr style="color:green;">
        <td>
                <a href="{link}">               
                <img src="{image}" alt="{title}" title="{title}" width="80" height="60"/>
                 <br/>
                 <xsl:value-of select="title"/>
                </a>
         </td>
      </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>