//package com.example.capstone_1.repository.search;
//
//import com.example.capstone_1.domain.QReportBoard;
//import com.example.capstone_1.domain.QReportReply;
//import com.example.capstone_1.domain.ReportBoard;
//import com.example.capstone_1.dto.BoardImageDTO;
//import com.example.capstone_1.dto.BoardListAllDTO;
//import com.example.capstone_1.dto.BoardListReplyCountDTO;
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.Tuple;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.JPQLQuery;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ReportBoardSearchImpl extends QuerydslRepositorySupport implements ReportBoardSearch{
//
//    public ReportBoardSearchImpl(){
//        super(ReportBoard.class);
//    }
//
//    @Override
//    public Page<ReportBoard> search1(Pageable pageable) {
//
//        QReportBoard board = QReportBoard.reportBoard;
//
//
//        JPQLQuery<ReportBoard> query = from(board);
//
//        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//
//        booleanBuilder.or(board.title.contains("11")); // title like ...
//
//        booleanBuilder.or(board.content.contains("11")); // content like ....
//
//        query.where(booleanBuilder);
//        query.where(board.bno.gt(0L));
//
//
//        //paging
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        List<ReportBoard> list = query.fetch();
//
//        long count = query.fetchCount();
//
//
//        return null;
//    }
//
//    @Override
//    public Page<ReportBoard> searchAll(String[] types, String keyword, Pageable pageable) {
//        QReportBoard board = QReportBoard.reportBoard;
//        JPQLQuery<ReportBoard> query = from(board);
//
//        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면
//
//            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//
//            for(String type: types){
//
//                switch (type){
//                    case "t":
//                        booleanBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(board.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(board.writer.contains(keyword));
//                        break;
//                }
//            }//end for
//            query.where(booleanBuilder);
//        }//end if
//
//        //bno > 0
//        query.where(board.bno.gt(0L));
//
//        //paging
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        List<ReportBoard> list = query.fetch();
//
//        long count = query.fetchCount();
//
//        return new PageImpl<>(list, pageable, count);
//
//    }
//
//    @Override
//    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
//        QReportBoard board = QReportBoard.reportBoard;
//        QReportReply reply = QReportReply.reportReply;
//
//        JPQLQuery<ReportBoard> query = from(board);
//        query.leftJoin(reply).on(reply.reportBoard.eq(board));
//
//        query.groupBy(board);
//
//        if( (types != null && types.length > 0) && keyword != null ){
//
//            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//
//            for(String type: types){
//
//                switch (type){
//                    case "t":
//                        booleanBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(board.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(board.writer.contains(keyword));
//                        break;
//                }
//            }//end for
//            query.where(booleanBuilder);
//        }
//
//        //bno > 0
//        query.where(board.bno.gt(0L));
//
//        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
//                board.bno,
//                board.title,
//                board.writer,
//                board.regDate,
//                reply.count().as("replyCount")
//        ));
//
//        this.getQuerydsl().applyPagination(pageable,dtoQuery);
//
//        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
//
//        long count = dtoQuery.fetchCount();
//
//        return new PageImpl<>(dtoList, pageable, count);
//    }
//
//    @Override
//    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//        QReportBoard board = QReportBoard.reportBoard;
//        QReportReply reply = QReportReply.reportReply;
//
//        JPQLQuery<ReportBoard> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.reportBoard.eq(board)); //left join
//
//        if( (types != null && types.length > 0) && keyword != null ){
//
//            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//
//            for(String type: types){
//
//                switch (type){
//                    case "t":
//                        booleanBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(board.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(board.writer.contains(keyword));
//                        break;
//                }
//            }//end for
//            boardJPQLQuery.where(booleanBuilder);
//        }
//
//        boardJPQLQuery.groupBy(board);
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging
//
//
//
//        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
//
//        List<Tuple> tupleList = tupleJPQLQuery.fetch();
//
//        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
//
//            ReportBoard board1 = (ReportBoard) tuple.get(board);
//            long replyCount = tuple.get(1,Long.class);
//
//            BoardListAllDTO dto = BoardListAllDTO.builder()
//                    .bno(board1.getBno())
//                    .title(board1.getTitle())
//                    .writer(board1.getWriter())
//                    .regDate(board1.getRegDate())
//                    .replyCount(replyCount)
//                    .build();
//
//            //BoardImage를 BoardImageDTO 처리할 부분
//            List<BoardImageDTO> imageDTOS = board1.getImageSetReportBoard().stream().sorted()
//                    .map(boardImage -> BoardImageDTO.builder()
//                            .uuid(boardImage.getUuid())
//                            .imageUrl(boardImage.getImageUrl())
//                            .ord(boardImage.getOrd())
//                            .build()
//                    ).collect(Collectors.toList());
//
//            dto.setBoardImages(imageDTOS);
//
//            return dto;
//        }).collect(Collectors.toList());
//
//        long totalCount = boardJPQLQuery.fetchCount();
//
//
//        return new PageImpl<>(dtoList, pageable, totalCount);
//    }
//}
