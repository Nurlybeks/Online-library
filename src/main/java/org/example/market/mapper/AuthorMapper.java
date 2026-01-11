package org.example.market.mapper;

import org.example.market.dto.AuthorDto;
import org.example.market.entity.Author;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);
    AuthorDto toDto(Author author);
    Author toEntity(AuthorDto authorDto);

    void updateAuthorFromDto(AuthorDto dto, @MappingTarget Author author);
}
