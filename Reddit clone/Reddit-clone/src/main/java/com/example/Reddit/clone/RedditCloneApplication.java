package com.example.Reddit.clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RedditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneApplication.class, args);
	}




}


/*







 */



/*
	community types:
	public - alle kan se og lage posts, kommentarer
	restricted - alle kan se posts og kommentarer, men bare medlemmer kan lage
	private - bare medlemmer kan se posts og kommentarer, og bare meldemmer kan lage, må bli invitert til community
	man kan lage bruker
	man kan endre brukernavn (hvis det ikke er tatt)
	man kan lage community
	man kan se posts i community
	man kan lage post på community (hvis man er del av community)
	man kan endre tittel og content i community post
	man kan lage bilde i post
	man kan endre bilde i post
	man få feed av posts fra de communitiene man er del av (man får opp postene som blir laget)
	man kan lage kommentar på community (trenger ikke å være del av community)
	man kan endre innhold i kommentar
	man kan se hvilke communities man er del av
	man kan unsubscribe fra communities
	man kan slette en community hvis man er admin der
	man kan sende forespørsel om å få bli med i community som er private/restricted
	man kan få opp forespørsler fra folk om å bli med
	man kan se hvilke posts man har laget
	man kan slette egne posts
	man kan slette andres posts i communitien hvis man er admin eller mod der
	man kan se hvilke kommentarer man har laget
	man kan slette kommentarer man har laget
	man kan slette andres kommentarer i communitien hvis man er admin eller mod der
	man kan lage profilbilde
	man kan lage bilde i community
	man kan lage wallpaper i community
	man kan banne folk fra community hvis man er admin eller mod der
	man kan lage moderatorer i community hvis man er admin der
	man kan fjerne mod rettigheter til mod
	man kan banne mods i community hvis man er admin der
	slette mod: bruker må være admin
	man kan godta forespørselene til å bli med i community
	man kan gjøre så andre ikke kan se egne kommentarer/posts
	hvis community er private, og den som ser ikke er medlem, så kan ikke brukeren se akuratt de kommentarene og postene i den communityen
man kan slette community
	man kan slette post
	hvis man sletter kommentar uten barn, så slettes kommentaren helt
	realtime chat
	design
tester
 */

























