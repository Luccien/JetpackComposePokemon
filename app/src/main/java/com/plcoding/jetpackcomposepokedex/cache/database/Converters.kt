package com.plcoding.jetpackcomposepokedex.cache.database

import androidx.room.TypeConverter
import com.plcoding.jetpackcomposepokedex.network.remote.responses.*

class Converters {



    @TypeConverter
    fun fromSprites(sprite: Sprites):String{
        return sprite.frontDefault.toString()
    }

    // only sprite.frontDefault  will be stored and restored ,  "a" is just to fill
    @TypeConverter
    fun toSprites(frontDefault:String):Sprites{
        return Sprites("a","a","a","a",frontDefault.toString(),"a","a","a", Other(DreamWorld("a","a"),
            OfficialArtwork("a")
        ), Versions(

            GenerationI(
                    RedBlue("a","a","a","a"),Yellow("a","a","a","a")),
            GenerationIi(
                    Crystal("a","a","a","a"), Gold("a","a","a","a"), Silver("a","a","a","a")
                    ),
            GenerationIii(
                    Emerald("a","a"), FireredLeafgreen("a","a","a","a"),RubySapphire("a","a","a","a")),
            GenerationIv(
                    DiamondPearl("a","a","a","a","a","a","a","a"),
                    HeartgoldSoulsilver("a","a","a","a","a","a","a","a"), Platinum("a","a","a","a","a","a","a","a")
                    ),
            GenerationV
                        (BlackWhite(Animated("a","a","a","a","a","a","a","a"),"a","a","a","a","a","a","a","a")), GenerationVi
                        (OmegarubyAlphasapphire("a","a","a","a"),XY("a","a","a","a")),
            GenerationVii(
                        Icons("a","a"), UltraSunUltraMoon("a","a","a","a")),
            GenerationViii(
                        IconsX("a","a"))
        )
        )
    }





// TODO ---- DELETE this if solved --- check is the @ColumnInfo(name = "stats") for the naming fun fromStats()    or type?
    // TODO this way is not optimal because it can cause errors when the   api (response models) changes but of course also the response models have to change than--> it will be noticed
    // TODO erorrs could be caused if name or url has following strings: ",,,,q" ";;;;q"

    @TypeConverter
    fun fromStats(stats:List<Stat>):String{
        var statString = ""
        for(i in stats.indices) {
            val stat = stats[i]
            statString +=stat.baseStat.toString()
            statString +=",,,,q"
            statString +=stat.effort.toString()
            statString +=",,,,q"
            statString +=stat.stat.name.toString()
            statString +=",,,,q"
            statString +=stat.stat.url.toString()
            statString +=";;;;x"
        }
        return statString
    }

    @TypeConverter
    fun toStats(statsS:String):List<Stat>{

        var stats:MutableList<Stat> = mutableListOf()

        // list of all stats
        val allStat = statsS.split(";;;;x").toTypedArray()
        for(i in allStat.indices) {
            // last index is empty
            if(i < allStat.size-1 ) {
                // list of stat values
                val statValues = allStat[i].split(",,,,q").toTypedArray()
                // put all 4 values in one single Stat
                val stat = Stat(
                    statValues[0].toInt(),
                    statValues[1].toInt(),
                    StatX(statValues[2].toString(), statValues[3].toString())
                )
                // add the stat object to the List
                stats.add(stat)
            }

        }

        return stats

    }

    //--------------

    @TypeConverter
    fun fromTypes(typs:List<Type>):String{
        var typString = ""
        for(i in typs.indices) {
            val typ = typs[i]
            typString +=typ.slot.toString()
            typString +=",,,,q"
            typString +=typ.type.name.toString()
            typString +=",,,,q"
            typString +=typ.type.url.toString()

        }
        return typString
    }

    @TypeConverter
    fun toTypes(typsS:String):List<Type>{

        var typs:MutableList<Type> = mutableListOf()

        // list of all typs
        val allTyp = typsS.split(";;;;x").toTypedArray()//
        //allTyp.removeLast()
        for(i in allTyp.indices) {
            // last index is empty
            if(i < allTyp.size-1 ) {
                // list of typ values
                val typValues = allTyp[i].split(",,,,q").toTypedArray()
                // put all 3 values in one sigle Typ
                val typ = Type(typValues[0].toInt(), TypeX(typValues[1], typValues[2]))
                // add the typ object to the List
                typs.add(typ)
            }

        }

        return typs
    }







    // TODO Check if Gson() converter is a  better choice...
    /*
    companion object {

        @JvmStatic
        @TypeConverter
        fun fromStat(stat: List<Stat>) = Gson().toJson(stat)

        @JvmStatic
        @TypeConverter
        fun toStat(s: String): Stat =
            Gson().fromJson(s, Stat::class.java)
    }
*/

}