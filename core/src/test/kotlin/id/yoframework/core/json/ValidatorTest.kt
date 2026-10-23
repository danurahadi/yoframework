package id.yoframework.core.json

import arrow.data.Validated
import id.yoframework.core.json.validator.equalTo
import id.yoframework.core.json.validator.error.ValidationError
import id.yoframework.core.json.validator.greaterThan
import id.yoframework.core.json.validator.lessThan
import id.yoframework.core.json.validator.notNull
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidatorTest {

    private fun <T> validatorValid(validated: Validated<ValidationError, T>, ops: (Validated.Valid<T>) -> Unit) {
        if (validated is Validated.Valid<T>) {
            ops(validated)
        } else {
            assertTrue(false)
        }
    }

    private val jsonObject = json {
        obj(
            "some_string" to "this is String",
            "some_integer" to 12,
            "some_boolean" to true,
            "some_double" to 0.0,
            "parent" to obj(
                "some_string" to "this is String",
                "some_integer" to 12,
                "some_boolean" to true,
                "some_double" to 0.0,
                "parent" to obj(
                    "some_string" to "this is String",
                    "some_integer" to 12,
                    "some_boolean" to true,
                    "some_double" to 0.0
                )
            )
        )
    }

    @Test
    fun `validator must able to check not null and return value based on required type`() {
        validatorValid(validated = jsonObject.notNull<String>("some_string")) {
            assertTrue(it.isValid)
            assertEquals(it.a, "this is String")
        }

        validatorValid(validated = jsonObject.notNull<Int>("some_integer")) {
            assertTrue(it.isValid)
            assertEquals(it.a, 12)
        }

        validatorValid(validated = jsonObject.notNull<Boolean>("some_boolean")) {
            assertTrue(it.isValid)
            assertEquals(it.a, true)
        }

        validatorValid(validated = jsonObject.notNull<Double>("some_double")) {
            assertTrue(it.isValid)
            assertEquals(it.a, 0.0)
        }
    }

    @Test
    fun `numeric comparison validators must compare values correctly`() {
        assertTrue(jsonObject.equalTo("some_integer", 12).isValid)
        assertTrue(jsonObject.equalTo("some_integer", 13).isInvalid)

        assertTrue(jsonObject.greaterThan("some_integer", 11).isValid)
        assertTrue(jsonObject.greaterThan("some_integer", 12).isInvalid)
        assertTrue(jsonObject.greaterThan("some_integer", 12, equals = true).isValid)

        assertTrue(jsonObject.lessThan("some_integer", 13).isValid)
        assertTrue(jsonObject.lessThan("some_integer", 11).isInvalid)
        assertTrue(jsonObject.lessThan("some_integer", 12).isInvalid)
        assertTrue(jsonObject.lessThan("some_integer", 12, equals = true).isValid)
    }
}
