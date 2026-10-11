package com.example.Reddit.clone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedditCloneApplicationTests {

	@Test
	void contextLoads() {
	}

}



/*
Tester
X Kunne lage fake repository - eller bruke det

Jwt
Få tak i token ved å autentisere bruker
Skjekke at kun den som har laget post, admin og mod kan slette post
Tester på en sånn måte at man kan laste opp rett til git, og at testene vil da gå av


JPA
Opprette bruker
Slette post - se om både post er slettet, og kommentarer, at post tittel er kontinuerlig
Se at bilder kan lagres og hentes
Skjekke at man kan lage unger til kommentar
Skjekke at messages lages når man gjør forskjellige ting - lager post, lager kommentar til post, sender chat beskjed
chat ting - skjekke at man kan lage en chat med en annen bruker, at de kan chatte sammen med sine linker, og at chatten funker da.
slette community, og at posts og kommentarer slettes da i samme slengen






 */