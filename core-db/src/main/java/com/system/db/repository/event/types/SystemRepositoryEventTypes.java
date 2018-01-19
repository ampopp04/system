package com.system.db.repository.event.types;

import com.system.logging.exception.SystemException;
import com.system.util.string.StringUtils;

public enum SystemRepositoryEventTypes {

    READ(true, false, false, false),
    INSERT(false, true, false, false),
    UPDATE(false, false, true, false),
    DELETE(false, false, false, true),

    /**
     * INSERT or UPDATE
     */
    SAVE(false, true, true, false),

    /**
     * INSERT, UPDATE, or DELETE
     */
    MODIFY(false, true, true, true),
    ALL(true, true, true, true),
    NONE(false, false, false, false),

    REPLACE(false, false, true, true),
    UPDATE_OR_DELETE(false, false, true, true),

    INSERT_OR_DELETE(false, true, false, true);

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private SystemRepositoryEventTypes(boolean read, boolean insert, boolean update, boolean delete) {
        this.read = read;
        this.insert = insert;
        this.update = update;
        this.delete = delete;
    }

    private boolean read;
    private boolean insert;
    private boolean update;
    private boolean delete;


    /**
     * @return the read
     */
    public boolean isRead() {
        return this.read;
    }


    /**
     * @return the insert
     */
    public boolean isInsert() {
        return this.insert;
    }


    /**
     * @return the update
     */
    public boolean isUpdate() {
        return this.update;
    }


    /**
     * @return the delete
     */
    public boolean isDelete() {
        return this.delete;
    }


    public static SystemRepositoryEventTypes fromName(String name) {
        return SystemRepositoryEventTypes.valueOf(sanitizeRawName(name));
    }

    private static String sanitizeRawName(String rawName) {
        //Check empty
        if (org.apache.commons.lang3.StringUtils.isEmpty(rawName)) {
            throw SystemException.newInstance("Event type is empty, please specify a valid processing type.");
        }

        //Handle case where raw name is correct, all capitals
        if (!StringUtils.contains(rawName, " ") && rawName.equals(rawName.toUpperCase())) {
            return rawName;
        }

        //Handle case where there are no spaces and no capital letters, just uppercase the name Ex. update = UPDATE
        if (!StringUtils.contains(rawName, " ") && rawName.equals(rawName.toLowerCase())) {
            return rawName.toUpperCase();
        }

        //Handle case where name is proper but spaced, must replace spaces with _ Ex. Insert or Delete = INSERT_OR_DELETE
        if (StringUtils.contains(rawName, " ")) {
            return rawName.replace(" ", "_").toUpperCase();
        }

        //Handle case where name is all one word with capitals Ex.  InsertOrDelete = INSERT_OR_DELETE
        if (!StringUtils.contains(rawName, " ") && !rawName.equals(rawName.toLowerCase())) {
            return StringUtils.addSpaceOnCapitialLetters(rawName).toUpperCase();
        }

        throw SystemException.newInstance("Cannot decode processing type, please ensure it is in the correct format. Type: [" + rawName + "]");

    }

    @Override
    public String toString() {
        return "SystemRepositoryEventTypes{" +
                "read=" + read +
                ", insert=" + insert +
                ", update=" + update +
                ", delete=" + delete +
                '}';
    }
}
