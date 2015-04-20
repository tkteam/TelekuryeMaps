package com.telekurye.tools;

import java.util.ArrayList;

public class Constant {

	public static class BagimsizTipi {

		public static final ArrayList<String> Name() {
			ArrayList<String> list = new ArrayList<String>();
			list.add("Mesken");
			list.add("Huzurevi");
			list.add("Öğrenci Yurdu");
			list.add("Misafirhane");
			list.add("Çocuk Yurdu");
			list.add("Otel");
			list.add("Banka Şubesi");
			list.add("PTT");
			list.add("Belediye");
			list.add("Valilik/Kaymakamlık");
			list.add("Muhtarlık");
			list.add("Noter");
			list.add("Sanayi");
			list.add("Okul, Üniversite, Araştırma");
			list.add("Hastane ve Bakım Kuruluşları");
			list.add("Eczane");
			list.add("İbadet veya Dini Faaliyetler");
			list.add("Polis");
			list.add("Silahlı Kuvvetler");
			list.add("Cezaevi/Tutukevi");
			list.add("İtfaiye");
			list.add("Kapıcı Dairesi");
			return list;
		}

		public static final ArrayList<Integer> Id() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(1);
			list.add(2);
			list.add(3);
			list.add(4);
			list.add(5);
			list.add(6);
			list.add(7);
			list.add(8);
			list.add(9);
			list.add(10);
			list.add(11);
			list.add(12);
			list.add(13);
			list.add(14);
			list.add(15);
			list.add(16);
			list.add(17);
			list.add(18);
			list.add(19);
			list.add(20);
			list.add(21);
			list.add(22);
			return list;
		}
	}

	public static class BuildingTypes {

		public static final ArrayList<String> Name() {
			ArrayList<String> list = new ArrayList<String>();
			list.add("Arsa"); //
			list.add("İnşaat"); //
			list.add("Mesken");
			list.add("Kamu");
			list.add("Özel İşyeri");
			list.add("Banka");
			list.add("Otel");
			list.add("PTT");
			list.add("Sanayi");
			list.add("Büfe");
			list.add("Geçici Yerleşim");
			list.add("Seyyar"); //
			list.add("Site Girişi"); //
			list.add("Yeraltı Çarşısı");
			list.add("Bina Dışı Yapı"); //
			list.add("Tahsis");
			list.add("Diğer");
			return list;
		}

		public static final ArrayList<Integer> Id() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(1);
			list.add(2);
			list.add(3);
			list.add(4);
			list.add(5);
			list.add(6);
			list.add(7);
			list.add(8);
			list.add(9);
			list.add(10);
			list.add(11);
			list.add(12);
			list.add(13);
			list.add(14);
			list.add(15);
			list.add(16);
			list.add(17);
			return list;
		}
	}

	public static class StreetTypes {

		public static final ArrayList<String> Name() {
			ArrayList<String> list = new ArrayList<String>();
			list.add("Köy Sokağı");
			list.add("Meydan");
			list.add("Bulvar");
			list.add("Cadde");
			list.add("Sokak");
			list.add("Küme Evler");
			return list;
		}

		public static final ArrayList<Integer> Id() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(0);
			list.add(1);
			list.add(2);
			list.add(3);
			list.add(4);
			list.add(5);
			return list;
		}

	}

}
