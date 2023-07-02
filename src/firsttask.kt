package org.jetbrains.kotlin.Math
val scan = java.util.Scanner(System.`in`)

class MutableInt(var value : Int){
    fun get() : Int{
        return value
    }
}
//реализация контейнера vector из c++ на базе списка
open class Vector<type_name : Any>{
    // реализация класса каждого элемента в списке, хранит в себе значение() и данные о следующем элементе
    class EachElement<type_name> (var value : type_name, var next_element : EachElement<type_name>? = null)
    // головной (первый) элемент списка
    open var head : EachElement<type_name>? = null

    // метод для вставки нового значения new_element в текущий список
    fun add(new_element : type_name){
        if (head == null){
            head = EachElement(new_element)
        }
        else {
            var current = head
            while (current?.next_element != null) {
                current = current.next_element
            }
            current?.next_element = EachElement(new_element)
        }
    }
    // перегруженный оператор для получения значения элемента в списке по его номеру
    operator fun get(index : Int) : type_name?{
        var current = head
        for (i in 0 until index) {
            if (current != null) {
                current = current.next_element
            }
            else {
                return null
            }
        }
        return current?.value
    }

    operator fun set(index : Int, value : type_name){
        if (index >=0){
            var current = head
            for (i in 0 until index){
                if (current != null) {
                    current = current.next_element
                }
                else {
                    println("такого индекса в списке нет")
                }
                if (current != null) {
                    current.value = value
                }
            }
        }
    }

    fun size() : Int{
        var current = head
        var size = 0
        while(current != null){
            current = current.next_element
            size++
        }
        return size
    }

    fun index(value : type_name) : Int{
        var current = head
        for (i in 0 until size()){
            if(current?.value == value)
                return i
            else{
                current = current?.next_element
            }
        }
        return -1
    }

    fun delete_element(index : Int){
        if (index in 0 until size()) {
            if (index == 0) {
                head = null
            } else {
                var current = head
                for (i in 0 until index - 1) {
                    current = current?.next_element
                }
                current?.next_element = current?.next_element?.next_element
            }
        }
    }

    override fun toString() : String {
        var current = this.head
        var string : String = ""
        for (i in 0 until size()){
            if (current == head){
                string+="${current?.value}"
            }
            else{
                string+="\n${current?.value}"
            }
            current = current?.next_element
        }
        return string
    }

    fun clear() {
        for (i in size() downTo 0){
            this.delete_element(i)
        }
    }
}
open class Square(var x : Int, var y : Int, var size : Int){
    override fun toString(): String {
        return "${x + 1} ${y + 1} $size"
    }

    fun get_first_size(new_N : Int, old_N : Int){
        this.x *= (old_N / new_N)
        this.y *= (old_N / new_N)
        this.size *= (old_N / new_N)
    }
}

fun find_koefficient(n : Int) : Int{
    for (i in 2 until n){
        if (n % i == 0){
            return i
        }
    }
    return n
}

fun clone(other_table : Vector<Square>, new_table : Vector<Square>){
    var current = other_table.head
    for (i in 0 until other_table.size()){
        val new_square : Square = Square(current?.value?.x!!,current.value.y,current.value.size)
        new_table.add(new_square)
        current = current.next_element
    }
}

fun get_first_size(table : Vector<Square>, new_N: Int, old_N: Int){
    for (i in 0 until table.size()){
        table.get(i)?.get_first_size(new_N, old_N)
    }
}

fun is_inside_old_squares(table : Vector<Square>,new_x : Int, new_y : Int) : Boolean{
    for (i in 0 until table.size()){
        if (new_x >= table.get(i)?.x!! && new_x < table.get(i)?.x!! + table.get(i)?.size!! && new_y >= table.get(i)?.y!! && new_y < table.get(i)?.y!! + table.get(i)?.size!!){
            return true
        }
    }
    return false
}

fun backtracking(table : Vector<Square>, start_x : MutableInt, start_y : MutableInt, record : MutableInt,record_table : Vector<Square>, area : Int, N : Int){

    for (x in start_x.value until N){
        for (y in start_y.value until N) {
            if (!is_inside_old_squares(table,x,y)){
                var new_length = Math.min(N - x, N - y)
                for (i in 0 until table.size()) {
                    if (table.get(i)?.x!! + table.get(i)?.size!! > x && table.get(i)?.y!! > y) {
                        new_length = Math.min(new_length, table.get(i)?.y!! - y)
                    }
                }
                for ( i in new_length downTo 1){
                    var new_Square = Square(x,y,i)
                    var copy_table : Vector<Square> = Vector<Square>()
                    clone(table,copy_table)
                    copy_table.add(new_Square)

                    if (area + new_Square.size * new_Square.size == N * N) {

                        if (copy_table.size() < record.value) {
                            record.value = copy_table.size()
                            record_table.clear()
                            clone(copy_table, record_table)
                        }
                    }
                    else{
                        if (copy_table.size() < record.value + 1){
                            backtracking(copy_table, MutableInt(x),MutableInt(y + i),record,record_table,area + new_Square.size * new_Square.size,N)
                        }
                        else{
                            return
                        }
                    }
                }
                return
            }
        }
        start_y.value = start_y.value.div(2)
    }
}

fun main() {
    val N = scan.nextInt()
    val new_N = find_koefficient(N)
    var half =Math.round( new_N / 2.0)
    var table : Vector<Square> = Vector<Square>()
    table.add(Square(0,0, half.toInt()))
    table.add(Square(0,half.toInt(),new_N - half.toInt()))
    table.add(Square(half.toInt(),0,new_N - half.toInt()))
    var record_table : Vector<Square> = Vector<Square>()
    var record = MutableInt(2 * N + 2)
    val area : Int = table.get(0)?.size!! * table.get(0)?.size!! + 2 * table.get(1)?.size!! * table.get(1)?.size!!
    backtracking(table, MutableInt(table.get(1)?.size!!), MutableInt(table.get(0)?.size!!),record,record_table,area,new_N)
    get_first_size(record_table, new_N, N)
    println(record.value)
    println(record_table)
}