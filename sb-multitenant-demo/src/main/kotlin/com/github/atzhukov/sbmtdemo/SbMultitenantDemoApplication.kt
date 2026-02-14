package com.github.atzhukov.sbmtdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SbMultitenantDemoApplication

fun main(args: Array<String>) {
	runApplication<SbMultitenantDemoApplication>(*args)
}
