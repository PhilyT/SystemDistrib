package com.m1miageprojet.annuaire;

import java.io.IOException;

/**
 * Created by ch-ha_000 on 11/01/2017.
 */
public class Main {


    public static void main(String[] args) {
        Annuaire annuaire = null;
        try {
            annuaire = new Annuaire();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            annuaire.Maj();
        }
    }

}
