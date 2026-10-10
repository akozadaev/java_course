package ru.akozadaev.library;
import java.util.*;
public class Book {
    public int id; public String title; public List<Author> authors=Collections.emptyList();
    public String authorNames(){if(authors==null||authors.isEmpty())return "Автор неизвестен";StringBuilder b=new StringBuilder();for(Author a:authors){if(b.length()>0)b.append(", ");b.append(a.name);}return b.toString();}
    public static class Author { public String name; }
}
