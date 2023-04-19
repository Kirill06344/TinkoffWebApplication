/*
 * This file is generated by jOOQ.
 */
package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records;


import java.beans.ConstructorProperties;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;

import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> implements Record1<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT.ID</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row1<Long> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row1<Long> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Chat.CHAT.ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public ChatRecord value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord values(@NotNull Long value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    @ConstructorProperties({ "id" })
    public ChatRecord(@NotNull Long id) {
        super(Chat.CHAT);

        setId(id);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos.Chat value) {
        super(Chat.CHAT);

        if (value != null) {
            setId(value.getId());
            resetChangedOnNotNull();
        }
    }
}
