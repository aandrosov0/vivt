import daos.ReviewDAO
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:sqlite:sample.db")
    Bot("7162702245:AAHv8oIZIM-MBHgBvxBKa-OlDcMGJpAJGr4", ReviewDAO()).run()
}