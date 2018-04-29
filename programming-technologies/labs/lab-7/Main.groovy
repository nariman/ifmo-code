/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.groovy
 * Created on: Apr 29, 2018
 */

import groovy.time.TimeCategory

class Main {
    def a

    static void main(String... args) {
        println "Welcome, Groovy!"

        //
        // Task 1
        //

        // First o = new First(first: true, second: 2, third: "3rd")
        First f = new First(true, 2, "3rd")
        println f.first
        println f.second
        println f.third

        // Second s = new Second()
        // println s.first
        // println s.second
        // println s.third

        println smth()

        //
        // Task 2
        //

        println first(null)
        // println second(null)

        BigDecimal fbd = new BigDecimal("1.2345")
        BigDecimal sbd = new BigDecimal("1.2345")

        println fbd.is(sbd)
        println fbd == sbd
        println fbd.equals(sbd)

        fbd = fbd.add(1).divide(2).multiply(3)
        sbd = (sbd + 1) / 2 * 3

        println fbd
        println sbd

        Main main1 = new Main()
        main1.test1()

        Main main2 = new Main()
        main2.test2()

        Calendar fcal = Calendar.instance
        fcal.set 2015, Calendar.FEBRUARY, 28
        Date fd = fcal.time

        Calendar scal = Calendar.instance
        scal.set 2015, Calendar.JANUARY, 31
        Date sd = scal.time

        println fcal
        println scal

        // use (TimeCategory) {
        //     println fcal - scal
        //     println fcal - 1.months + 1.days + 1.months
        // }

        //
        // Task 3
        //

        Closure divide = { x, y -> x / y }
        Closure subtract = { x, y -> x - y }
        Closure wow = { x, y, z -> subtract(divide(x, y), z) }

        println divide(5, 2)
        println subtract(5, 2)
        println wow(5, 2, 1)
    }

    static Integer smth() {
        1
    }

    static Integer first(Integer toReturn) {
        return toReturn
    }

    static Integer Second(int toReturn) {
        return toReturn
    }

    void test1() {
        a = 7
        a = 't'
        a = "test"
    }

    void test2() {
        a = "test"
        a = 't'
        a = 7
    }
}
