package com.example.assignment6.Model

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseBuilder {
    private var INSTANCE: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movies_list"
                ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("""
INSERT INTO movies_list (movie_id, title, vote_average, vote_count, poster_path, backdrop_path, popularity, overview, original_language, original_title, release_date) VALUES
(439079, 'The Nun', 6.2, 222, '/sFC1ElvoKGdHJIWRpNB3xWJ9lJA.jpg', '/fgsHxz21B27hOOqQBiw9L6yWcM7.jpg', 176.945, 'When a young nun at a cloistered abbey in Romania takes her own life, a priest with a haunted past and a novitiate on the threshold of her final vows are sent by the Vatican to investigate. Together they uncover the order''s unholy secret. Risking not only their lives but their faith and their very souls, they confront a malevolent force in the form of the same demonic nun that first terrorized audiences in "The Conjuring 2," as the abbey becomes a horrific battleground between the living and the damned.', 'en', 'The Nun', '2018-09-05'),
(345940, 'The Meg', 6.2, 789, '/xqECHNvzbDL5I3iiOVUkVPJMSbc.jpg', '/rH79sB6Nkx4cMW3JzsUy7wK0rhX.jpg', 152.834, 'A deep sea submersible pilot revisits his past fears in the Mariana Trench, and accidentally unleashes the seventy foot ancestor of the Great White Shark believed to be extinct.', 'en', 'The Meg', '2018-08-09'),
(507569, 'The Seven Deadly Sins: Prisoners of the Sky', 5.6, 20, '/r6pPUVUKU5eIpYj4oEzidk5ZibB.jpg', '/uKwOX7MtKlAaGeCQe6c4jc1vZpj.jpg', 123.771, 'The Seven Deadly Sins travel to a remote land in search of the phantom ingredient "sky fish." Meliodas and Hawk end up at a "Sky Palace" that exists above the clouds, where all the residents have wings. Meliodas is mistaken for a boy who committed a crime and is thrown in prison. Meanwhile, the residents are preparing a ceremony for defense against a ferocious beast that awakens once every 3,000 years. But the Six Knights of Black, a Demon Clan army, arrives and removes the seal on the beast in order to threaten the lives of the residents of Sky Palace. Meliodas and his allies meet the Six Knights of Black in battle.', 'ja', 'The Seven Deadly Sins: Prisoners of the Sky', '2018-08-18'),
(402900, 'Ocean''s Eight', 6.9, 1540, '/MvYpKlpFukTivnlBhizGbkAe3v.jpg', '/scQf03Fm3jeyv4FH04qvi4fp4wh.jpg', 98.434, 'Debbie Ocean, a criminal mastermind, gathers a crew of female thieves to pull off the heist of the century at New York''s annual Met Gala.', 'en', 'Ocean''s Eight', '2018-06-07'),
(353081, 'Mission: Impossible - Fallout', 7.3, 1406, '/AkJQpZp9WoNdj7pLYSj1L0RcMMN.jpg', '/5qxePyMYDisLe8rJiBYX8HKEyv2.jpg', 72.577, 'When an IMF mission ends badly, the world is faced with dire consequences. As Ethan Hunt takes it upon himself to fulfil his original briefing, the CIA begin to question his loyalty and his motives. The IMF team find themselves in a race against time, hunted by assassins while trying to prevent a global catastrophe.', 'en', 'Mission: Impossible - Fallout', '2018-07-25'),
(399360, 'Alpha', 5.4, 160, '/afdZAIcAQscziqVtsEoh2PwsYTW.jpg', '/nKMeTdm72LQ756Eq20uTjF1zDXu.jpg', 67.883, 'After a hunting expedition goes awry, a young caveman struggles against the elements to find his way home.', 'en', 'Alpha', '2018-08-17'),
(363088, 'Ant-Man and the Wasp', 7.0, 1987, '/rv1AWImgx386ULjcf62VYaW8zSt.jpg', '/6P3c80EOm7BodndGBUAJHHsHKrp.jpg', 65.764, 'As Scott Lang awaits expiration of his term of house detention, Hope van Dyne and Dr. Hank Pym involve him in a scheme to rescue Mrs. van Dyne from the micro-universe into which she has fallen, while two groups of schemers converge on them with intentions of stealing Dr. Pym''s inventions.', 'en', 'Ant-Man and the Wasp', '2018-07-04'),
(493922, 'Hereditary', 6.9, 874, '/dscvG9AWLOzxedHcXN7QguJRhsP.jpg', '/pS9Aub8MPyQbENblGD8mHeaqMuv.jpg', 63.602, 'When Ellen, the matriarch of the Graham family, passes away, her daughter''s family begins to unravel cryptic and increasingly terrifying secrets about their ancestry. The more they discover, the more they find themselves trying to outrun the sinister fate they seem to have inherited.', 'en', 'Hereditary', '2018-06-04'),
(442249, 'The First Purge', 6.0, 639, '/2slvblTroiT1lY9bYLK7Amigo1k.jpg', '/dnaitaoCh8MftfYEVnprcuYExZp.jpg', 47.882, 'To push the crime rate below one percent for the rest of the year, the New Founding Fathers of America test a sociological theory that vents aggression for one night in one isolated community. But when the violence of oppressors meets the rage of the others, the contagion will explode from the trial-city borders and spread across the nation.', 'en', 'The First Purge', '2018-07-04'),
(260513, 'Incredibles 2', 7.7, 2221, '/x1txcDXkcM65gl7w20PwYSxAYah.jpg', '/mabuNsGJgRuCTuGqjFkWe1xdu19.jpg', 45.156, 'Elastigirl springs into action to save the day, while Mr. Incredible faces his greatest challenge yet – taking care of the problems of his three children.', 'en', 'Incredibles 2', '2018-06-14'),
(500664, 'Upgrade', 7.3, 381, '/8fDtXi6gVw8WUMWGT9XFz7YwkuE.jpg', '/22cUd4Yg5euCxIwWzXrL4m4otkU.jpg', 43.978, 'A brutal mugging leaves Grey Trace paralyzed in the hospital and his beloved wife dead. A billionaire inventor soon offers Trace a cure — an artificial intelligence implant called STEM that will enhance his body. Now able to walk, Grey finds that he also has superhuman strength and agility — skills he uses to seek revenge against the thugs who destroyed his life.', 'en', 'Upgrade', '2018-06-01'),
(455980, 'Tag', 6.9, 509, '/eXXpuW2xaq5Aen9N5prFlARVIvr.jpg', '/yRXzrwLfB5tDTIA3lSU9S3N9RUK.jpg', 42.722, 'For one month every year, five highly competitive friends hit the ground running in a no-holds-barred game of tag they’ve been playing since the first grade. This year, the game coincides with the wedding of their only undefeated player, which should finally make him an easy target. But he knows they’re coming...and he’s ready.', 'en', 'Tag', '2018-05-30'),
(429300, 'Adrift', 6.4, 495, '/5gLDeADaETvwQlQow5szlyuhLbj.jpg', '/64jAqTJvrzEwncD3ARZdqYLcqbc.jpg', 42.032, 'A true story of survival, as a young couple''s chance encounter leads them first to love, and then on the adventure of a lifetime as they face one of the most catastrophic hurricanes in recorded history.', 'en', 'Adrift', '2018-05-31'),
(458594, 'Peppermint', 7.0, 25, '/jrzxS0vcbzIIay1sdYm0rgI2QfJ.jpg', '/5Bh1u5dtn8g6AY0qf2TRQxJTtS0.jpg', 41.574, 'A grieving mother transforms herself into a vigilante following the murders of her husband and daughter, eluding the authorities to deliver her own personal brand of justice.', 'en', 'Peppermint', '2018-09-06'),
(345887, 'The Equalizer 2', 6.3, 477, '/cQvc9N6JiMVKqol3wcYrGshsIdZ.jpg', '/z6KLDE72SxE1A8JcJTmmnWArOvR.jpg', 35.717, 'Robert McCall returns to deliver his special brand of vigilante justice -- but how far will he go when it''s someone he loves?', 'en', 'The Equalizer 2', '2018-07-19'),
(455207, 'Crazy Rich Asians', 6.9, 180, '/gnTqi4nhIi1eesT5uYMmhEPGNih.jpg', '/zeHB7aP46Xs3u4aFLuAq2GFeUGb.jpg', 30.168, 'An American-born Chinese economics professor accompanies her boyfriend to Singapore for his best friend''s wedding, only to get thrust into the lives of Asia''s rich and famous.', 'en', 'Crazy Rich Asians', '2018-08-15');
""".trimIndent());


                        }
                    })
                    .build()
            }
        }
        return INSTANCE!!
    }

    fun getDatabasePath(context: Context): String? {
        return getInstance(context).openHelper.writableDatabase.path
    }
    fun closeDatabase() {
        INSTANCE?.close()
        INSTANCE = null
    }
}

