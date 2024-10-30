import daos.ReviewDAO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect("jdbc:sqlite:sample.db")

    transaction {
        SchemaUtils.create(ReviewDAO.Reviews)
    }

    Bot(
        token = "",
        reviewDAO = ReviewDAO()
    ).run()
}