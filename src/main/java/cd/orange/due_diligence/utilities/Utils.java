package cd.orange.due_diligence.utilities;


import cd.orange.due_diligence.entities.DueDiligence;
import cd.orange.due_diligence.entities.Users;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static Users users;
    private static  Integer incre=0;
    public static String getImage() {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyHHMMddmmsss");
        final Date now = new Date();
        incre++;
        final String ref = "DUE_DOC"+sdfDate.format(now)+""+incre+".";
        return ref;
    }
    public static String getMo() {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyHHMMddmmsss");
        final Date now = new Date();
        final String ref = sdfDate.format(now);
        return ref;
    }
    public static   String formatHtmlNotif(String s){
        String html="<!DOCTYPE >\n" +
                "\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body style=  \"font-family: Arial;\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=  \"0\" cellspacing=  \"0\" style=  \"width: 600px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<p align=\"center\" style=  \"font-family: Arial, sans-serif; font-size: 10px; color: #000;\"> </p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td bgcolor=  \"#d5d5d5\" style=  \"border: #cccccc 1px solid; padding: 15px;\" valign=  \"top\">\n" +
                "<table border=  \"0\" cellpadding=  \"0\" cellspacing=  \"0\" style=  \"width: 600px; height: 200px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td style=  \"border: 1px solid #cccccc; padding: 0px; text-align: center;\">\n" +
                "<img alt=  \"emailHeader\" height=  \"200\" src=  \"https://developer.orange.com/wp-content/uploads/ODemailBanner_4rali8jc.jpg\" title=  \"emailheaders_helloagain_rgb01_690\" width=  \"600\"/></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table border=  \"0\" cellpadding=  \"0\" cellspacing=  \"0\" style=  \"width: 600px; height: 18px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td height=  \"15\"> </td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table  cellpadding=  \"0\" cellspacing=  \"0\" style=  \"width: 600px; height: 515px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td align=  \"left\" bgcolor=  \"#ffffff\" style=  \"font-family: arial, helvetica, sans-serif; font-size: 16px; line-height: normal; color: #000000; border: #cccccc 1px solid; padding: 20px;\">\n" +
                "<p><img alt=  \"logo Orange\" id=  \"sugar_text_logo\" src=  \"https://developer.orange.com/wp-content/uploads/OrangeLogo32x32_ig6ps0zv.jpg\" style=  \"width: 32px; height: 32px; padding-right:2%;\"/><strong><span style=  \"font-family: arial, helvetica, sans-serif;\">Portail Fournisseur</span></strong></p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"> </p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"><span style=  \"font-size:11pt\"><span style=\"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\">Bonjour {{nom}},</span></span></span></p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt;color: #f16e00; font: 100 16px Arial, sans-serif;\"><span style=  \"font-size: 11pt\"><span style=  \"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\">Merci d'avoir postulé pour une demande de partenariat. Nous allons examiner attentivement votre candidature et vous tiendrons informé.e des prochaines étapes. </span></span></span></p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"><span style=  \"font-size:11pt\"><span style=  \"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\">Vous pouvez également vous connecter à votre portail candidat. Cela vous permettra de suivre l’évolution de votre candidature, de comprendre les actions que vous devez entreprendre et de retirer votre candidature si vous le souhaitez.</span></span></span></p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"><span style=  \"font-size:11pt\"><span style=\"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\">Visitez notre site pour en savoir plus sur nos produits et services sur <a href=\"https://www.orange.cd/\" style=  \"color: #f16e00; font: 100 14px Arial, sans-serif;\">Orange</a> </span></span></span></p>\n" +
                "\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"> </p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"><span style=  \"font-size:11pt\"><span style=  \"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\">Pour toute question, vous pouvez contacter  </span><a href=\"mailto:1999.info@orange.com\" style=\"color: #2e58ff; text-decoration: none;\"><u>1999.info@orange.com</u></a></span></span></p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"> </p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"><span style=  \"font-size:11pt\"><span style= \"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\">Cordialement,</span></span></span></p>\n" +
                "<p style=  \"margin:0cm 0cm 8pt\"><span style=  \"font-size:11pt\"><span style= \"line-height:107%\"><span style=  \"font-family:Calibri,sans-serif\"><b>L'équipe <span style=  \"color:#ed7d31\">Direction d'    Achats et logistiques</span></b>.</span></span></span></p>\n" +
                "<p> </p>\n" +
                "<p style=  \"color: #f16e00; font: 100 18px Arial, sans-serif;\">Suivez nous</p=\n" +
                ">\n" +
                "<p><a href=  \"https://easymail.orange-business.com/click/gAAAAABgGpkP2_13UO\n" +
                "tLt-XuDMBBFdC6Bd2J0shod6qn2fGJKnJLCTMQbFhXPnt1O8LgtabjNSMgDQ5gqn380V072KUmP\n" +
                "z5ilRuak0I3WlsyiAOdVkyMipFZchwqbTjhO9f1lFldGzzOV5uYyflHZeypEb68jQeMpjjwC3Vv\n" +
                "trLnC5zcKyTWfO6r5Pm9Z2hLHrxDwH01KehxsEpftM4d-qVjTaFNQMVf37lMiK8TAgze5Pirx2E\n" +
                "-qr10LsnAqdg7iU5bbZVDnSzZIaK6oOpU7MPAG31We_c2ukYaMR-AAGBKrxPXdu1oepE=  \" title=  \"Twitter\"><img alt=  \"twitter\" border=  \"0\" height=  \"47\" src=  \"https://developer.orange.com/wp-content/uploads/NL-footer-social-TwitterBlack_jz6ecmvp.png\" style=  \"border: none;\" width=  \"48\"/></a> <a href=  \"https://easymail.orange-business.com/click/gAAAAABgGpkPVBW3QyLox6_RdZBTAOKfsOUHNc5Wx4LGwJttDASFDG-6vTZcpnUCu8CYA0hSUkNEoqEbovlilWKo9FDytWGtHuRz8bqyCx08msLiUyE4CD4KOvnoqiKu7weUiqF9JA12XKIEEzvjj8vPlv5bfYIZihU_iyXaHS17f8Q31m4-plyixS3PfB9wKF4eVQYqm9iFbKYYWBUnVs9xK18JPE_ZOhTmUj4XwtQFMcd53iYcqG9k3XLxmMaKFyPrKFYXiILm-wne8VEWHMa0eI8Ce60rf06SAapAi0jZNPLk6fvKLfvXW_H5ifN31E5Kc8nxdNAb\" title= \"LinkedIn\"><img alt=  \"linkedin\" border=  \"0\" height=  \"47\" src=  \"https://developer.orange.com/wp-content/uploads/NL-footer-social-LinkedinBlack_0ke4sjaq.png\" style=  \"border: none;\" width=  \"48\"/></a> <a href=  \"https://easymail.orange-business.com/click/gAAAAABgGpkPfzidI99lxfAtlcDKF5yUvnPDlbIQ7GVGgCLt6-B4esnzN2DbMaXUXXGt9QOQ707Kyi9iW28Gf9cIKGM-wDo-UXxacA58U3aReeJRMXpC3rJ4wITgAa8y8kRF9Vltau7Clq2DR4HmTee6plWuOI6C8i8MNs6NOoAXvrzvrV9FvHv0L66xskxDXOA3fwhoLyOh8hPrNoWGjl0ZkkzH0cwi_aWK_FmpcF61IdlhPXynZ1RfPiAMJfvLx-MfeQvbXS0FZtYeYfLM2DwNhKWHuiG6M8x9J__518Hb6Ch7GDM7rkg=  \" title=  \"YouTube\"><img alt=  \"dailymotion\" border=  \"0\" height=  \"47\" src=  \"https://developer.orange.com/wp-content/uploads/NL-footer-social-YouTubeBlack_nthz2m9r.png\" style= \"border: none;\" width=  \"48\"/></a></p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table border=  \"0\" cellpadding=  \"0\" cellspacing=  \"0\" style=  \"width: 600px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td height=  \"15\"> </td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table border=  \"0\" cellpadding=  \"0\" cellspacing=  \"0\" style=  \"width: 600px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td align=  \"left\" bgcolor=  \"#d5d5d5\" style=  \"font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: normal; color: #000000;\">Orange 2023. All rights reserved.</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td align=  \"left\" bgcolor=  \"#d5d5d5\" style=  \"padding-top: 15px; font-family: Arial, Helvetica, sans-serif; font-size: 11px; line-height: normal; color: #000000;\">\n" +
                "<p> </p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        html=html.replace("{{nom}}",s);
        return  html;
    }


    public static Integer getDateDiff(Date date2) {
        return   (int)( (new Date().getTime() - date2.getTime())
                / (1000 * 60 * 60 * 24) );
    }
    public static Boolean checkFic(Integer i,Double valeur){

        if(i==7||i==8||i==17||i==18||i==19||i==20||i==21){
            return  true;
        }else if(i==1&&valeur>=300000){
            return  true;
        } else if ((i==11||i==14)&&valeur>=100000) {
            return  true;
        } else if (((i>1&&i<7)||i==9||i==10||i==12||i==13||i==15||i==16)&&valeur>=75000) {
            return  true;
        }
        return  false;
    }
    public static  Date getDate(Date d,Integer delay){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, delay);
        Date dt = c.getTime();
        return dt;
    }

    public static  String getName(DueDiligence d,String s){
        if(d!=null){
            String []paths=d.getPaths().split(";");
            String []names=d.getParams().split(";");

            for(Integer i=0;i<paths.length;i++ ){
                if(paths[i].equalsIgnoreCase(s)){
                    return names[i].trim();
                }
            }
        }

        return null;
    }

}
