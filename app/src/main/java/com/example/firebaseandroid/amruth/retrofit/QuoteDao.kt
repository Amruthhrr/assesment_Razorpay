import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firebaseandroid.models.QuoteItem

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteItem>)

    @Query("SELECT * FROM quotes")
    fun getQuotes(): List<QuoteItem>
}