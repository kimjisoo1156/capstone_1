package com.example.capstone_1.repository.search;

import com.example.capstone_1.domain.NoticeBoard;
import com.example.capstone_1.domain.QNoticeBoard;
import com.example.capstone_1.domain.QNoticeReply;
import com.example.capstone_1.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class NoticeBoardSearchImpl  extends QuerydslRepositorySupport implements NoticeBoardSearch{

    public NoticeBoardSearchImpl(){
        super(NoticeBoard.class);
    }


    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QNoticeBoard board = QNoticeBoard.noticeBoard;
        QNoticeReply noticeReply = QNoticeReply.noticeReply;


        JPQLQuery<NoticeBoard> query = from(board);
        query.leftJoin(noticeReply).on(noticeReply.noticeBoard.eq(board));

        query.groupBy(board);

        if( (types != null && types.length > 0) && keyword != null ){

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }

        //bno > 0
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                noticeReply.count().as("replyCount")
        ));

        this.getQuerydsl().applyPagination(pageable,dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }


    @Override
    public Page<NoticeBoard> search1(Pageable pageable) {
        QNoticeBoard board = QNoticeBoard.noticeBoard;


        JPQLQuery<NoticeBoard> query = from(board);

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

        booleanBuilder.or(board.title.contains("11")); // title like ...

        booleanBuilder.or(board.content.contains("11")); // content like ....

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));


        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<NoticeBoard> list = query.fetch();

        long count = query.fetchCount();


        return null;
    }

    @Override
    public Page<NoticeBoard> searchAll(String[] types, String keyword, Pageable pageable) {
        QNoticeBoard board = QNoticeBoard.noticeBoard;

        JPQLQuery<NoticeBoard> query = from(board);

        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }//end if

        //bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<NoticeBoard> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }
}
