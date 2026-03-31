package com.pao.laboratory05.playlist;

import java.util.Arrays;
import java.util.Comparator;public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    Playlist(String name) {this.name=name;}//— constructor

    String getName()
    {
        return this.name;
    }

    void addSong(Song song) {
        Song[] newSongs = new Song[songs.length + 1];
        System.arraycopy(songs, 0, newSongs, 0, songs.length);
        newSongs[songs.length] = song;
        this.songs = newSongs;
        //—adaugă cu pattern - ul de resize (System.arraycopy)
    }
    void printSortedByTitle()
    {
        Song[] copy = songs.clone();
        Arrays.sort(copy, Comparator.comparing(Song::title) );
        for (var x : copy)
        {
            System.out.println(x);
        }
        //— clonează array-ul, Arrays.sort(copy), afișează
    }
    void printSortedByDuration()
    {
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        for (var x : copy)
        {
            System.out.println(x);
        }
        //— clonează, Arrays.sort(copy, new SongDurationComparator()), afișeaz
    }
    int getTotalDuration()
    {
        System.out.println();
        int suma =0;
        for (var x : songs)
        {
            suma += x.durationSeconds();
        }
        return suma;
    }
}
