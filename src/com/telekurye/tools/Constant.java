package com.telekurye.tools;

import java.util.ArrayList;

public class Constant {

	public static class BagimsizTipi {

		public static final ArrayList<String> Name() {
			ArrayList<String> list = new ArrayList<String>();
			list.add("Mesken");
			list.add("Huzurevi");
			list.add("��renci Yurdu");
			list.add("Misafirhane");
			list.add("�ocuk Yurdu");
			list.add("Otel");
			list.add("Banka �ubesi");
			list.add("PTT");
			list.add("Belediye");
			list.add("Valilik/Kaymakaml�k");
			list.add("Muhtarl�k");
			list.add("Noter");
			list.add("Sanayi");
			list.add("Okul, �niversite, Ara�t�rma");
			list.add("Hastane ve Bak�m Kurulu�lar�");
			list.add("Eczane");
			list.add("�badet veya Dini Faaliyetler");
			list.add("Polis");
			list.add("Silahl� Kuvvetler");
			list.add("Cezaevi/Tutukevi");
			list.add("�tfaiye");
			list.add("Kap�c� Dairesi");
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
			list.add("�n�aat"); //
			list.add("Mesken");
			list.add("Kamu");
			list.add("�zel ��yeri");
			list.add("Banka");
			list.add("Otel");
			list.add("PTT");
			list.add("Sanayi");
			list.add("B�fe");
			list.add("Ge�ici Yerle�im");
			list.add("Seyyar"); //
			list.add("Site Giri�i"); //
			list.add("Yeralt� �ar��s�");
			list.add("Bina D��� Yap�"); //
			list.add("Tahsis");
			list.add("Di�er");
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
			list.add("K�y Soka��");
			list.add("Meydan");
			list.add("Bulvar");
			list.add("Cadde");
			list.add("Sokak");
			list.add("K�me Evler");
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
