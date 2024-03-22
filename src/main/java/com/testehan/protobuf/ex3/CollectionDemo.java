package com.testehan.protobuf.ex3;

import com.testehan.models.ex03.Book;
import com.testehan.models.ex03.Library;

public class CollectionDemo {
    public static void main(String[] args) {
        var book1 = Book.newBuilder()
                .setAuthor("JK Rowling")
                .setTitle("Harry potter")
                .setPublicationYear(1998).build();
        var book2 = Book.newBuilder()
                .setAuthor("J. R. R. Tolkien")
                .setTitle("Fellowship of the Ring")
                .setPublicationYear(1954).build();

        var library = Library.newBuilder().setName("My Fantastic books")
                        .addBooks(book1)
                                .addBooks(book2)
                                        .build();
        System.out.println(library);
    }
}
