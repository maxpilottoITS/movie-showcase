package com.maxpilotto.movieshowcase.services;

import android.content.Context;

import com.maxpilotto.kon.JsonArray;
import com.maxpilotto.kon.JsonObject;
import com.maxpilotto.movieshowcase.models.Genre;
import com.maxpilotto.movieshowcase.models.Movie;
import com.maxpilotto.movieshowcase.protocols.MovieResultCallback;
import com.maxpilotto.movieshowcase.protocols.MovieUpdateCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.maxpilotto.movieshowcase.util.Util.getPoster;

/**
 * Data provider for this application
 */
public final class DataProvider {
    private static DataProvider instance;

    public static void init(Context context) {
        instance = new DataProvider();
    }

    public static DataProvider get() {
        return instance;
    }

    private DataProvider() {
    }

    public void getMovie(Integer id, MovieResultCallback callback) {
        getMovies(localCopy -> {
            for (Movie m : localCopy) {
                if (m.getId().equals(id)){
                    callback.onFind(m);
                    return;
                }
            }

            callback.onFind(null);
        });
    }

    public void getMovies(MovieUpdateCallback callback) {
        String input = "{\"page\":1,\"total_results\":10000,\"total_pages\":500,\"results\":[{\"popularity\":725.223,\"vote_count\":828,\"video\":false,\"poster_path\":\"/8WUVHemHFH2ZIP6NWkwlHWsyrEL.jpg\",\"id\":338762,\"adult\":false,\"backdrop_path\":\"/lP5eKh8WOcPysfELrUpGhHJGZEH.jpg\",\"original_language\":\"en\",\"original_title\":\"Bloodshot\",\"genre_ids\":[28,878],\"title\":\"Bloodshot\",\"vote_average\":7.3,\"overview\":\"After he and his wife are murdered, marine Ray Garrison is resurrected by a team of scientists. Enhanced with nanotechnology, he becomes a superhuman, biotech killing machine—'Bloodshot'. As Ray first trains with fellow super-soldiers, he cannot recall anything from his former life. But when his memories flood back and he remembers the man that killed both him and his wife, he breaks out of the facility to get revenge, only to discover that there's more to the conspiracy than he thought.\",\"release_date\":\"2020-02-20\"},{\"popularity\":614.199,\"vote_count\":2715,\"video\":false,\"poster_path\":\"/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg\",\"id\":419704,\"adult\":false,\"backdrop_path\":\"/5BwqwxMEjeFtdknRV792Svo0K1v.jpg\",\"original_language\":\"en\",\"original_title\":\"Ad Astra\",\"genre_ids\":[12,18,9648,878,53],\"title\":\"Ad Astra\",\"vote_average\":6,\"overview\":\"The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.\",\"release_date\":\"2019-09-17\"},{\"popularity\":439.289,\"vote_count\":7887,\"video\":false,\"poster_path\":\"/eivQmS3wqzqnQWILHLc4FsEfcXP.jpg\",\"id\":363088,\"adult\":false,\"backdrop_path\":\"/6P3c80EOm7BodndGBUAJHHsHKrp.jpg\",\"original_language\":\"en\",\"original_title\":\"Ant-Man and the Wasp\",\"genre_ids\":[28,12,35,878],\"title\":\"Ant-Man and the Wasp\",\"vote_average\":7,\"overview\":\"Just when his time under house arrest is about to end, Scott Lang once again puts his freedom at risk to help Hope van Dyne and Dr. Hank Pym dive into the quantum realm and try to accomplish, against time and any chance of success, a very dangerous rescue mission.\",\"release_date\":\"2018-07-04\"},{\"popularity\":376.929,\"vote_count\":4747,\"video\":false,\"poster_path\":\"/AkJQpZp9WoNdj7pLYSj1L0RcMMN.jpg\",\"id\":353081,\"adult\":false,\"backdrop_path\":\"/aw4FOsWr2FY373nKSxbpNi3fz4F.jpg\",\"original_language\":\"en\",\"original_title\":\"Mission: Impossible - Fallout\",\"genre_ids\":[28,12],\"title\":\"Mission: Impossible - Fallout\",\"vote_average\":7.3,\"overview\":\"When an IMF mission ends badly, the world is faced with dire consequences. As Ethan Hunt takes it upon himself to fulfill his original briefing, the CIA begin to question his loyalty and his motives. The IMF team find themselves in a race against time, hunted by assassins while trying to prevent a global catastrophe.\",\"release_date\":\"2018-07-13\"},{\"popularity\":318.531,\"vote_count\":894,\"video\":false,\"poster_path\":\"/4U7hpTK0XTQBKT5X60bKmJd05ha.jpg\",\"id\":570670,\"adult\":false,\"backdrop_path\":\"/uZMZyvarQuXLRqf3xdpdMqzdtjb.jpg\",\"original_language\":\"en\",\"original_title\":\"The Invisible Man\",\"genre_ids\":[27,878,53],\"title\":\"The Invisible Man\",\"vote_average\":7.1,\"overview\":\"When Cecilia's abusive ex takes his own life and leaves her his fortune, she suspects his death was a hoax. As a series of coincidences turn lethal, Cecilia works to prove that she is being hunted by someone nobody can see.\",\"release_date\":\"2020-02-26\"},{\"popularity\":316.815,\"vote_count\":2107,\"video\":false,\"poster_path\":\"/h4VB6m0RwcicVEZvzftYZyKXs6K.jpg\",\"id\":495764,\"adult\":false,\"backdrop_path\":\"/pbOOOT0ASXjP4aJZr5NyOjK9qix.jpg\",\"original_language\":\"en\",\"original_title\":\"Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)\",\"genre_ids\":[28,35,80],\"title\":\"Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)\",\"vote_average\":7.1,\"overview\":\"After her breakup with the Joker, Harley Quinn joins forces with singer Black Canary, assassin Huntress, and police detective Renee Montoya to help a young girl named Cassandra, who had a hit placed on her after she stole a rare diamond from crime lord Roman Sionis.\",\"release_date\":\"2020-02-05\"},{\"popularity\":228.373,\"vote_count\":2677,\"video\":false,\"poster_path\":\"/bB42KDdfWkOvmzmYkmK58ZlCa9P.jpg\",\"id\":512200,\"adult\":false,\"backdrop_path\":\"/hreiLoPysWG79TsyQgMzFKaOTF5.jpg\",\"original_language\":\"en\",\"original_title\":\"Jumanji: The Next Level\",\"genre_ids\":[28,12,35,14],\"title\":\"Jumanji: The Next Level\",\"vote_average\":6.8,\"overview\":\"As the gang return to Jumanji to rescue one of their own, they discover that nothing is as they expect. The players will have to brave parts unknown and unexplored in order to escape the world’s most dangerous game.\",\"release_date\":\"2019-12-04\"},{\"popularity\":224.563,\"vote_count\":11976,\"video\":false,\"poster_path\":\"/5vHssUeVe25bMrof1HyaPyWgaP.jpg\",\"id\":245891,\"adult\":false,\"backdrop_path\":\"/iJlGxN0p1suzloBGvvBu3QSSlhT.jpg\",\"original_language\":\"en\",\"original_title\":\"John Wick\",\"genre_ids\":[28,53],\"title\":\"John Wick\",\"vote_average\":7.2,\"overview\":\"Ex-hitman John Wick comes out of retirement to track down the gangsters that took everything from him.\",\"release_date\":\"2014-10-22\"},{\"popularity\":222.033,\"vote_count\":4886,\"video\":false,\"poster_path\":\"/2bXbqYdUdNVa8VIWXVfclP2ICtT.jpg\",\"id\":420818,\"adult\":false,\"backdrop_path\":\"/nRXO2SnOA75OsWhNhXstHB8ZmI3.jpg\",\"original_language\":\"en\",\"original_title\":\"The Lion King\",\"genre_ids\":[12,10751],\"title\":\"The Lion King\",\"vote_average\":7.1,\"overview\":\"Simba idolizes his father, King Mufasa, and takes to heart his own royal destiny. But not everyone in the kingdom celebrates the new cub's arrival. Scar, Mufasa's brother—and former heir to the throne—has plans of his own. The battle for Pride Rock is ravaged with betrayal, tragedy and drama, ultimately resulting in Simba's exile. With help from a curious pair of newfound friends, Simba will have to figure out how to grow up and take back what is rightfully his.\",\"release_date\":\"2019-07-12\"},{\"popularity\":213.017,\"vote_count\":882,\"video\":false,\"poster_path\":\"/xnjvwfDulnOCy8qtYX0iqydmMhk.jpg\",\"id\":448119,\"adult\":false,\"backdrop_path\":\"/xcUf6yIheo78btFqihlRLftdR3M.jpg\",\"original_language\":\"en\",\"original_title\":\"Dolittle\",\"genre_ids\":[12,35,14,10751],\"title\":\"Dolittle\",\"vote_average\":6.8,\"overview\":\"After losing his wife seven years earlier, the eccentric Dr. John Dolittle, famed doctor and veterinarian of Queen Victoria’s England, hermits himself away behind the high walls of Dolittle Manor with only his menagerie of exotic animals for company. But when the young queen falls gravely ill, a reluctant Dolittle is forced to set sail on an epic adventure to a mythical island in search of a cure, regaining his wit and courage as he crosses old adversaries and discovers wondrous creatures.\",\"release_date\":\"2020-01-01\"},{\"popularity\":209.854,\"vote_count\":3578,\"video\":false,\"poster_path\":\"/db32LaOibwEliAmSL2jjDF6oDdj.jpg\",\"id\":181812,\"adult\":false,\"backdrop_path\":\"/jOzrELAzFxtMx2I4uDGHOotdfsS.jpg\",\"original_language\":\"en\",\"original_title\":\"Star Wars: The Rise of Skywalker\",\"genre_ids\":[28,12,878],\"title\":\"Star Wars: The Rise of Skywalker\",\"vote_average\":6.5,\"overview\":\"The surviving Resistance faces the First Order once again as the journey of Rey, Finn and Poe Dameron continues. With the power and knowledge of generations behind them, the final battle begins.\",\"release_date\":\"2019-12-18\"},{\"popularity\":206.314,\"vote_count\":1018,\"video\":false,\"poster_path\":\"/8ZX18L5m6rH5viSYpRnTSbb9eXh.jpg\",\"id\":619264,\"adult\":false,\"backdrop_path\":\"/uNiTCaBIpw4vrRGEFKFm3VSt8Sm.jpg\",\"original_language\":\"es\",\"original_title\":\"El hoyo\",\"genre_ids\":[18,27,878,53],\"title\":\"The Platform\",\"vote_average\":7.1,\"overview\":\"A mysterious place, an indescribable prison, a deep hole. Two inmates living on each level. An unknown number of levels. A descending platform containing food for all of them. An inhuman fight for survival, but also an opportunity for solidarity…\",\"release_date\":\"2019-11-08\"},{\"popularity\":200.746,\"vote_count\":732,\"video\":false,\"poster_path\":\"/z4A6mFOLTMZAhCSPRyrtzG0SPbd.jpg\",\"id\":475303,\"adult\":false,\"backdrop_path\":\"/6fkqwqLEcDZOEAnBBfKAniwNxtx.jpg\",\"original_language\":\"en\",\"original_title\":\"A Rainy Day in New York\",\"genre_ids\":[35,10749],\"title\":\"A Rainy Day in New York\",\"vote_average\":6.6,\"overview\":\"Two young people arrive in New York to spend a weekend, but once they arrive they're met with bad weather and a series of adventures.\",\"release_date\":\"2019-07-26\"},{\"popularity\":195.888,\"vote_count\":478,\"video\":false,\"poster_path\":\"/3rQx8kJNfIZnlbILHUbRzK2HOng.jpg\",\"id\":508439,\"adult\":false,\"backdrop_path\":\"/xFxk4vnirOtUxpOEWgA1MCRfy6J.jpg\",\"original_language\":\"en\",\"original_title\":\"Onward\",\"genre_ids\":[12,16,35,14,10751],\"title\":\"Onward\",\"vote_average\":7.8,\"overview\":\"In a suburban fantasy world, two teenage elf brothers embark on an extraordinary quest to discover if there is still a little magic left out there.\",\"release_date\":\"2020-02-29\"},{\"popularity\":187.709,\"vote_count\":1429,\"video\":false,\"poster_path\":\"/y95lQLnuNKdPAzw9F9Ab8kJ80c3.jpg\",\"id\":38700,\"adult\":false,\"backdrop_path\":\"/upUy2QhMZEmtypPW3PdieKLAHxh.jpg\",\"original_language\":\"en\",\"original_title\":\"Bad Boys for Life\",\"genre_ids\":[28,80,53],\"title\":\"Bad Boys for Life\",\"vote_average\":6.5,\"overview\":\"Marcus and Mike are forced to confront new threats, career changes, and midlife crises as they join the newly created elite team AMMO of the Miami police department to take down the ruthless Armando Armas, the vicious leader of a Miami drug cartel.\",\"release_date\":\"2020-01-15\"},{\"popularity\":171.158,\"vote_count\":3335,\"video\":false,\"poster_path\":\"/h6Wi81XNXCjTAcdstiCLRykN3Pa.jpg\",\"id\":330457,\"adult\":false,\"backdrop_path\":\"/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg\",\"original_language\":\"en\",\"original_title\":\"Frozen II\",\"genre_ids\":[12,16,10751],\"title\":\"Frozen II\",\"vote_average\":7.1,\"overview\":\"Elsa, Anna, Kristoff and Olaf head far into the forest to learn the truth about an ancient mystery of their kingdom.\",\"release_date\":\"2019-11-20\"},{\"popularity\":167.132,\"vote_count\":544,\"video\":false,\"poster_path\":\"/jtrhTYB7xSrJxR1vusu99nvnZ1g.jpg\",\"id\":522627,\"adult\":false,\"backdrop_path\":\"/9Qfawg9WT3cSbBXQgDRuWbYS9lj.jpg\",\"original_language\":\"en\",\"original_title\":\"The Gentlemen\",\"genre_ids\":[28,35,80],\"title\":\"The Gentlemen\",\"vote_average\":7.9,\"overview\":\"American expat Mickey Pearson has built a highly profitable marijuana empire in London. When word gets out that he’s looking to cash out of the business forever it triggers plots, schemes, bribery and blackmail in an attempt to steal his domain out from under him.\",\"release_date\":\"2019-12-16\"},{\"popularity\":163.647,\"vote_count\":2975,\"video\":false,\"poster_path\":\"/kbC6I0AOSLTHFA2dieyat9h8QHP.jpg\",\"id\":39538,\"adult\":false,\"backdrop_path\":\"/57kqAPdVJTAJ4rnDTSVDx0f1JBu.jpg\",\"original_language\":\"en\",\"original_title\":\"Contagion\",\"genre_ids\":[18,878,53],\"title\":\"Contagion\",\"vote_average\":6.4,\"overview\":\"As an epidemic of a lethal airborne virus - that kills within days - rapidly grows, the worldwide medical community races to find a cure and control the panic that spreads faster than the virus itself.\",\"release_date\":\"2011-09-08\"},{\"popularity\":157.442,\"vote_count\":17,\"video\":false,\"poster_path\":\"/iaTW57RK87v7ZrGkLKVofNH9YOr.jpg\",\"id\":505951,\"adult\":false,\"backdrop_path\":\"/4zJbHJOshnAlQecHY5QjDvZQjeZ.jpg\",\"original_language\":\"ta\",\"original_title\":\"கண்ணும் கண்ணும் கொள்ளையடித்தால்\",\"genre_ids\":[35,18,10749],\"title\":\"Kannum Kannum Kollaiyadithaal\",\"vote_average\":5.8,\"overview\":\"Siddarth falls head-over-heels in love with Meera but things take a risky turn when they get into a messy situation with a dangerous man.\",\"release_date\":\"2020-02-28\"},{\"popularity\":154.414,\"vote_count\":6,\"video\":false,\"poster_path\":\"/mew5d3ZzFfVxFyPZ8ydJb18UOpr.jpg\",\"id\":625985,\"adult\":false,\"backdrop_path\":\"/j4B8G5yKRnTO655uvpQBXt4Ok08.jpg\",\"original_language\":\"ja\",\"original_title\":\"仮面ライダージオウNEXT TIME：ゲイツ、マジェスティ\",\"genre_ids\":[28,12,878],\"title\":\"Kamen Rider Zi-O NEXT TIME: Geiz, Majesty\",\"vote_average\":7,\"overview\":\"Geiz, Majesty is the first installment of the Kamen Rider Zi-O NEXT TIME series of V-Cinema films for Kamen Rider Zi-O. It focuses on the character Geiz Myokoin.\",\"release_date\":\"2020-02-28\"}]}";
        JsonArray json = new JsonObject(input).getJsonArray("results");
        List<Movie> movies = json.toObjectList(obj -> {
            System.out.println(obj);

            return new Movie(
                    obj.getInt("id"),
                    obj.getString("title"),
                    obj.getString("overview"),
                    obj.getCalendar("release_date", "yyyy-MM-dd", Locale.getDefault()),
                    getPoster(obj.getString("poster_path")),
                    getPoster(obj.getString("backdrop_path")),
                    parseGenre(obj.getIntList("genre_ids")),
                    obj.getInt("vote_average")
            );
        });

        callback.onLoad(movies);
    }

    @Deprecated
    public List<Genre> parseGenre(List<Integer> genres) {
        List<Genre> result = new ArrayList<>();

        for (Integer i : genres) {
            result.add(new Genre(i, "Test"));
        }

        return result;
    }
}
