<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/data">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <title>Bienvenido</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #F4F3EF; font-family: Arial, Helvetica, sans-serif; color: #2C241E;">

                <!-- CONTENEDOR PRINCIPAL EXTERNO -->
                <table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color: #F4F3EF; padding: 40px 0;">
                    <tr>
                        <td align="center">

                            <!-- TARJETA BLANCA CENTRADA -->
                            <table width="460" cellpadding="0" cellspacing="0" border="0" style="background-color: #FFFFFF; border-radius: 16px; padding: 40px 30px; text-align: center; box-shadow: 0 4px 12px rgba(0,0,0,0.05); width: 100%; max-width: 460px;">

                                <!-- 1. LOGO -->
                                <tr>
                                    <td align="center" style="padding-bottom: 20px;">
                                        <!-- Usamos {logoUrl} para inyectar la URL que viene desde el servicio -->
                                        <img url="{logoUrl}"
                                             alt="Logo Dulce Hogar"
                                             width="160"
                                             style="display: block; border: 0; outline: none; text-decoration: none; margin: 0 auto;"/>
                                    </td>
                                </tr>

                                <!-- 2. TÍTULO -->
                                <tr>
                                    <td align="center" style="font-size: 26px; font-weight: bold; color: #2C241E; padding-bottom: 12px; line-height: 1.2;">
                                        ¡Bienvenid@ a la Familia, <xsl:value-of select="nombre"/>!
                                    </td>
                                </tr>

                                <!-- 3. SUBTÍTULO -->
                                <tr>
                                    <td align="center" style="font-size: 15px; color: #555555; padding-bottom: 25px; line-height: 1.5;">
                                        <xsl:value-of select="mensaje"/>
                                    </td>
                                </tr>

                                <!-- 4. CAJA DE DATOS DEL USUARIO -->
                                <tr>
                                    <td style="padding-bottom: 25px;">
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color: #F9F8F6; border: 1px solid #E2E0D8; border-radius: 10px; padding: 18px; text-align: left; font-size: 14px; color: #2C241E;">
                                            <tr>
                                                <td style="padding-bottom: 10px; font-size: 14px; color: #2C241E;">
                                                    <strong>Correo registrado:</strong>&#160;
                                                    <a href="#" style="color: #2C241E !important; text-decoration: none !important; pointer-events: none; cursor: default;">
                                                        <xsl:value-of select="email"/>
                                                    </a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-bottom: 10px; font-size: 14px; color: #2C241E;">
                                                    <strong>Usuario:</strong>&#160;
                                                    <a href="#" style="color: #2C241E !important; text-decoration: none !important; pointer-events: none; cursor: default;">
                                                        <xsl:value-of select="user"/>
                                                    </a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="font-size: 14px;">
                                                    <strong>Código de activación:</strong>&#160;<span style="background-color: #FDE4CB; color: #2C241E; padding: 3px 8px; border-radius: 4px; font-weight: bold;"><xsl:value-of select="codigo"/></span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr style="font-size: 12px; color: #2C241E !important; color: #888888; margin-bottom:5px;">
                                    <td>¡Ahora tendrás todos los beneficios de tu perfil en línea!</td>
                                </tr>

                                <!-- 5. BOTÓN PRINCIPAL -->
                                <tr>
                                    <td align="center" style="padding-bottom: 25px;">
                                        <a href="{url}" style="background-color: #7C876D; color: #FFFFFF !important; padding: 15px 25px; border-radius: 25px; font-size: 18px; font-weight: bold; text-decoration: none; display: block; text-align: center;">
                                            Confirmar Mi Cuenta
                                        </a>
                                    </td>
                                </tr>

                                <!-- 6. PIE DE PÁGINA -->
                                <tr>
                                    <td align="center" style="font-size: 12px; color: #888888; line-height: 1.4;">
                                        Si no creaste esta cuenta, puedes ignorar este correo tranquilamente.
                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>