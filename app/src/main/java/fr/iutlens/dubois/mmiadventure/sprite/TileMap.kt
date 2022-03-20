package fr.iutlens.dubois.mmiadventure.sprite

interface TileMap {
    fun get(x : Int , y : Int) : Int
    val sizeX : Int
    val sizeY : Int
}