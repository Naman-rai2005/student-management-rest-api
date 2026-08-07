package com.namanrai.sms.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityMappingTest {

    @Test
    void relationshipsUseLazyFetching() throws Exception {
        assertEquals(FetchType.LAZY,
                Student.class.getDeclaredField("department")
                        .getAnnotation(jakarta.persistence.ManyToOne.class).fetch());
        assertEquals(FetchType.LAZY,
                Student.class.getDeclaredField("course")
                        .getAnnotation(jakarta.persistence.ManyToOne.class).fetch());
        assertEquals(FetchType.LAZY,
                Student.class.getDeclaredField("address")
                        .getAnnotation(OneToOne.class).fetch());
        assertEquals(FetchType.LAZY,
                Department.class.getDeclaredField("students")
                        .getAnnotation(OneToMany.class).fetch());
        assertEquals(FetchType.LAZY,
                Course.class.getDeclaredField("students")
                        .getAnnotation(OneToMany.class).fetch());
        assertEquals(FetchType.LAZY,
                Address.class.getDeclaredField("student")
                        .getAnnotation(OneToOne.class).fetch());
    }

    @Test
    void studentEmailHasDatabaseUniquenessConstraint() throws Exception {
        var column = Student.class.getDeclaredField("email")
                .getAnnotation(jakarta.persistence.Column.class);
        assertTrue(column.unique());

        var table = Student.class.getAnnotation(jakarta.persistence.Table.class);
        assertTrue(java.util.Arrays.stream(table.uniqueConstraints())
                .anyMatch(c -> c.name().equals("uk_student_email")
                        && java.util.Arrays.asList(c.columnNames()).contains("email")));
    }
}
