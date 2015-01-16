package com.telekurye.tools;

import java.util.ArrayList;

public class Constant {

	public static class BuildingTypes {

		public static final ArrayList<String> Name() {
			ArrayList<String> list = new ArrayList<String>();
			list.add("Arsa");
			list.add("İnşaat");
			list.add("Mesken");
			list.add("Kamu");
			list.add("Özel İşyeri");
			list.add("Banka");
			list.add("Otel");
			list.add("PTT");
			list.add("Sanayi");
			list.add("Büfe");
			list.add("Geçici Yerleşim");
			list.add("Seyyar");
			list.add("Site Girişi");
			list.add("Yeraltı Çarşısı");
			list.add("Bina Dışı Yapı");
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
