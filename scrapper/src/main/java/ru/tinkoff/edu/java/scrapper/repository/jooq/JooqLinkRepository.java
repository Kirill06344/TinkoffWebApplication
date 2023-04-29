package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.utils.DataChangeState;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINK;


@Slf4j
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext context;

    public JooqLinkRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<Link> findLinkByUrl(String url) {
        LinkRecord link = context.selectFrom(LINK).where(LINK.URL.eq(url)).fetchOne();
        return (link != null) ? Optional.of(convertLinkRecordToLink(link)) : Optional.empty();
    }

    @Override
    public Optional<Link> findLinkById(long id) {
        LinkRecord link = context.selectFrom(LINK).where(LINK.ID.eq(id)).fetchOne();
        return (link != null) ? Optional.of(convertLinkRecordToLink(link)) : Optional.empty();
    }

    @Override
    public void deleteLinkByUrl(String url) {
        context.deleteFrom(LINK).where(LINK.URL.eq(url)).execute();
    }

    @Override
    public List<Link> findAllOldLinks() {
        Result<LinkRecord> result = context.selectFrom(LINK)
                .where(minute(currentLocalDateTime().minus(LINK.CHECKED_AT)).ge(1))
                .fetch();
        return convertResultToList(result);
    }

    @Override
    public void updateCheckedAtTime(long id) {
        context.update(LINK).set(LINK.CHECKED_AT, LocalDateTime.now()).where(LINK.ID.eq(id)).execute();
    }

    @Override
    public DataChangeState updateOtherData(long id, LocalDateTime updatedAt, long count) {
        var link = findLinkById(id);
        if (link.isEmpty()){
            return DataChangeState.NOTHING;
        }

        LinkRecord newData = context.update(LINK)
                .set(LINK.UPDATED_AT, updatedAt)
                .set(LINK.INTR_COUNT, count)
                .where(LINK.ID.eq(id))
                .returning(LINK.ID, LINK.URL, LINK.CHECKED_AT, LINK.UPDATED_AT, LINK.INTR_COUNT)
                .fetchOne();

        LocalDateTime prevDate = link.get().getUpdatedAt();
        LocalDateTime nextDate = newData.getUpdatedAt();

        long prevCount = link.get().getIntersectingCountField();
        long nextCount = newData.getIntrCount();

        if (prevCount < nextCount) {
            return DataChangeState.COUNT;
        } else if (!prevDate.equals(nextDate)) {
            return DataChangeState.OTHER;
        } else {
            return DataChangeState.NOTHING;
        }

    }

    @Override
    public Optional<Link> add(Link entity) {
        if (entity == null) {
            return Optional.empty();
        }

        LinkRecord link = context.insertInto(LINK, LINK.URL, LINK.CHECKED_AT, LINK.UPDATED_AT, LINK.INTR_COUNT)
                .values(entity.getUrl(), entity.getCheckedAt(), entity.getUpdatedAt(), entity.getIntersectingCountField())
                .onDuplicateKeyIgnore()
                .returning(LINK.ID, LINK.URL, LINK.CHECKED_AT, LINK.UPDATED_AT, LINK.INTR_COUNT)
                .fetchOne();

        return (link != null) ? Optional.of(convertLinkRecordToLink(link)) : findLinkByUrl(entity.getUrl());
    }

    @Override
    public List<Link> findAll() {
        Result<LinkRecord> result = context.selectFrom(LINK).fetch();
        return convertResultToList(result);
    }

    @Override
    public int deleteById(Long id) {
        return context.delete(LINK).where(LINK.ID.eq(id)).execute();
    }

    private Link convertLinkRecordToLink(LinkRecord record) {
        return new Link().setId(record.getId())
                .setUrl(record.getUrl())
                .setUpdatedAt(record.getUpdatedAt())
                .setIntersectingCountField(record.getIntrCount())
                .setCheckedAt(record.getCheckedAt());
    }

    private List<Link> convertResultToList(Result<LinkRecord> result) {
        List<Link> links = new ArrayList<>();
        if (result != null) {
            for (LinkRecord l : result) {
                links.add(convertLinkRecordToLink(l));
            }
        }
        return links;
    }
}
