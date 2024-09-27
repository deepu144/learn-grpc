package com.deepu.grpc.book.service;

import com.deepu.grpc.book.entity.Book;
import com.deepu.grpc.book.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import com.deepu.grpc.book.service.BookServiceGrpc.BookServiceImplBase;
import com.deepu.grpc.book.model.BookRequestOuterClass.BookRequest;
import com.deepu.grpc.book.model.BookResponseOuterClass.BookResponse;
import com.deepu.grpc.book.model.BookOuterClass;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class BookService extends BookServiceImplBase {
    public final BookRepository bookRepository;

    public void getBooks(BookRequest request,
                         StreamObserver<BookResponse> responseObserver) {
        List<Long> bookIds = request.getUniqueIdList();
        List<Book> books = bookRepository.findAllByUniqueIdIn(bookIds);

        responseObserver.onNext(getAllBookResponse(books));
        responseObserver.onCompleted();
    }

    public BookResponse getAllBookResponse(List<Book> books){
        List<BookOuterClass.Book> bookList =  books.stream()
                .map(this::convertToResponseBookProto)
                .toList();
        return BookResponse.newBuilder()
                .addAllBooks(bookList)
                .build();
    }

    public BookOuterClass.Book convertToResponseBookProto(Book book){
        BookOuterClass.Book.Builder book1 = BookOuterClass.Book.newBuilder();
        book1.setUniqueId(book.getUniqueId());
        book1.setBook(book.getBook());
        book1.setAuthor(book.getAuthor());
        book1.setYear(book.getPublishedYear());
        return book1.build();
    }
}
