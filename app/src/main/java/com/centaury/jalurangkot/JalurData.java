package com.centaury.jalurangkot;

import java.util.ArrayList;

/**
 * Created by Centaury on 27/01/2018.
 */

public class JalurData {
    public static String[][] data = new String[][]{
            {"Lyn-C (Blauran)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25406/content_lyn_c-_semua.png"},
            {"Lyn-C (IndPura)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25406/content_lyn_c-_semua.png"},
            {"Lyn-D", "http://www.surabaya.go.id/uploads/pictures/2017/2/25407/content_lyn_d.png"},
            {"Lyn-E (Balongsari)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25408/content_lyn_e-_semua.png"},
            {"Lyn-E (Simorukun)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25408/content_lyn_e-_semua.png"},
            {"Lyn-E (Sawahan)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25408/content_lyn_e-_semua.png"},
            {"Lyn-F", "http://www.surabaya.go.id/uploads/pictures/2017/2/25409/content_lyn_f.png"},
            {"Lyn-G (Karang Menjangan)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png"},
            {"Lyn-G (Karang Pilang)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png"},
            {"Lyn-G (Lakarsantri)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png"},
            {"Lyn-G (Wisma Lidah Kulon)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png"},
            {"Lyn-H1", "http://www.surabaya.go.id/uploads/pictures/2017/2/25411/content_lyn_h1.png"},
            {"Lyn-H2", "http://www.surabaya.go.id/uploads/pictures/2017/2/25412/content_lyn_h2.png"},
            {"Lyn-O (Pogot)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25421/content_lyn_o-pogot.png"},
            {"Lyn-O", "http://www.surabaya.go.id/uploads/pictures/2017/2/25422/content_lyn_o.png"},
    };

    public static ArrayList<JalurRute> getListData(){
        JalurRute jalurRute = null;
        ArrayList<JalurRute> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            jalurRute = new JalurRute();
            jalurRute.setName(data[i][0]);
            jalurRute.setPhoto(data[i][1]);

            list.add(jalurRute);
        }

        return list;
    }
}
