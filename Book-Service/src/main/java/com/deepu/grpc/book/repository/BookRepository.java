package com.deepu.grpc.book.repository;

import com.deepu.grpc.book.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId> {
    List<Book> findAllByUniqueIdIn(List<Long> uniqueIds);
}
