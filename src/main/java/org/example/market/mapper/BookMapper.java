package org.example.market.mapper;

import org.example.market.dto.BookDto;
import org.example.market.entity.Book;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
    Book toEntity(BookDto bookDto);
    BookDto toDto(Book book);

}
