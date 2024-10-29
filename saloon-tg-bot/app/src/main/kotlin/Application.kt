import daos.ReviewDAO
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:sqlite:sample.db")
    Bot(
        token = "7162702245:AAEXzJkjjgdx94jrCb0ScsrSgxcc6xSiaU0",
        reviewDAO = ReviewDAO()
    ).run()
}