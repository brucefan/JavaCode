package com.brucefan.code.generic;

/**
 * Created by bruce01.fan on 2016/2/14.
 */
public class ArenaFight {

    public void win(Kongfu<? extends Fist> kongfu1, Kongfu<? extends Foot> kongfu2) {
    }

    public void win2(Kongfu<Fist> kongfu1, Kongfu<Foot> kongfu2) {

    }

    public static void main(String[] args) {
        Kongfu<Fist> boxing = new Boxing<>();
        Kongfu<Foot> taekwondo = new Taekwondo<>();
        boxing.study(new Fist());
        taekwondo.study(new Foot());

    }
}
