package com.centaury.jalurangkot;

import java.util.ArrayList;

/**
 * Created by Centaury on 27/01/2018.
 */

public class JalurData {
    public static String[][] data = new String[][]{
            {"Lyn-C (Blauran)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25406/content_lyn_c-_semua.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-C (IndPura)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25406/content_lyn_c-_semua.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-D", "http://www.surabaya.go.id/uploads/pictures/2017/2/25407/content_lyn_d.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-E (Balongsari)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25408/content_lyn_e-_semua.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-E (Simorukun)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25408/content_lyn_e-_semua.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-E (Sawahan)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25408/content_lyn_e-_semua.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-F", "http://www.surabaya.go.id/uploads/pictures/2017/2/25409/content_lyn_f.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-G (Karang Menjangan)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-G (Karang Pilang)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-G (Lakarsantri)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-G (Wisma Lidah Kulon)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25410/content_lyn_g.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-H1", "http://www.surabaya.go.id/uploads/pictures/2017/2/25411/content_lyn_h1.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-H2", "http://www.surabaya.go.id/uploads/pictures/2017/2/25412/content_lyn_h2.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-O (Pogot)", "http://www.surabaya.go.id/uploads/pictures/2017/2/25421/content_lyn_o-pogot.png", String.valueOf(R.string.desc_lyn_o)},
            {"Lyn-O", "http://www.surabaya.go.id/uploads/pictures/2017/2/25422/content_lyn_o.png", String.valueOf(R.string.desc_lyn_o)},
    };

    public static ArrayList<JalurRute> getListData(){
        JalurRute jalurRute = null;
        ArrayList<JalurRute> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            jalurRute = new JalurRute();
            jalurRute.setName(data[i][0]);
            jalurRute.setPhoto(data[i][1]);
            jalurRute.setDesc(data[i][2]);

            list.add(jalurRute);
        }

        return list;
    }
}
