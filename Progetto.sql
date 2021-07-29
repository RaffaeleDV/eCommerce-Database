DROP DATABASE IF EXISTS progetto;
CREATE DATABASE progetto;
USE progetto;

DROP TABLE IF EXISTS utente;
CREATE TABLE IF NOT EXISTS utente(
	cf					 varchar(20) not null,
	nome				 varchar(25) not null,
	cognome 			 varchar(25) not null,
	CAP 				 varchar(10) not null,
	indirizzo_spedizione varchar(30) not null,
	dataNascita 		 date not null,
	pin 				 varchar(10) not null,
	data_scadenza_carta	 date not null,
	CVV 				 varchar(3) not null,
	primary key (cf)
);

DROP TABLE IF EXISTS commento;
CREATE TABLE IF NOT EXISTS commento(
	descrizione_com		 varchar(100),
	voto 				 integer(2),
	data_commento 		 date,
	commentatore 		 varchar(20) not null,
	commentato 			 varchar(20) not null,
    primary key(commentatore, commentato),
	foreign key (commentatore) references utente(cf)
				ON DELETE CASCADE ON UPDATE CASCADE,
	foreign key (commentato) references utente(cf)
				ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS prodotto_fisico;
CREATE TABLE IF NOT EXISTS prodotto_fisico(	
	codice_prod 		 varchar(15) not null,
	prezzo_prod			 integer not null,
	descrizione_prod	 varchar(100) ,
	venditore_prodotto	 varchar(20) not null,
	primary key (codice_prod),
	foreign key(venditore_prodotto) references utente(cf)
				ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS ordine;
CREATE TABLE IF NOT EXISTS ordine(
	codice_ordine		 varchar(15) not null,
	data_ordine          date not null,
	data_scad_ordine	 date not null,
	tipo_spedizione      varchar(20) not null,
    primary key(codice_ordine)
);	

DROP TABLE IF EXISTS software;
CREATE TABLE IF NOT EXISTS software(
	codice_software		 varchar(15) not null,
	prezzo_software		 integer not null,
	acquirente_software	 varchar(20) not null,
	venditore_software   varchar(20) not null,
	ordine_software		 varchar(15) not null,
	primary key(codice_software),
	foreign key (acquirente_software) references utente(cf)
				ON DELETE CASCADE ON UPDATE CASCADE,
	foreign key (venditore_software) references utente(cf)
	 			ON DELETE CASCADE ON UPDATE CASCADE,
	foreign key (ordine_software) references ordine(codice_ordine)
				ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS prototipo_di;
CREATE TABLE IF NOT EXISTS prototipo_di(
	software_r 			 varchar(15) not null,
	prototipo            varchar(15) not null,
	foreign key (software_r) references software(codice_software)
				ON DELETE CASCADE ON UPDATE CASCADE,
	foreign key (prototipo) references software(codice_software)
				ON DELETE CASCADE ON UPDATE CASCADE
);				

DROP TABLE IF EXISTS fattura;
CREATE TABLE IF NOT EXISTS fattura(	
	compratore_fattura	 varchar(20) not null,
	prodotto_fattura     varchar(15) not null,
	ordine_fattura       varchar(15) not null,
	foreign key (compratore_fattura) references utente(cf)
				ON DELETE CASCADE ON UPDATE CASCADE,
	foreign key (prodotto_fattura) references prodotto_fisico(codice_prod)
				ON DELETE CASCADE ON UPDATE CASCADE,
	foreign key (ordine_fattura) references ordine(codice_ordine)
				ON DELETE CASCADE ON UPDATE CASCADE			
);

DROP TABLE IF EXISTS corriere;
CREATE TABLE IF NOT EXISTS corriere(	
	nome_azienda    	 varchar(25) not null,
	p_iva				 varchar(15) not null,
	indirizzo_ente       varchar(25) not null,
	telefono             varchar(15),
    primary key (p_iva)
);

DROP TABLE IF EXISTS consegna;
CREATE TABLE IF NOT EXISTS consegna(
	ordine_consegna		 varchar(15) not null,
	fase				 varchar(10) not null,
	data_consegna        date not null,
	tipo_consegna		 varchar(10) not null,	
	codice_consegna 	 varchar(15) not null,
    codice_corriere      varchar(15), 
	primary key(codice_consegna),
	foreign key (ordine_consegna) references ordine(codice_ordine)
				ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key (codice_corriere) references corriere(p_iva)
				ON DELETE CASCADE ON UPDATE CASCADE            
        
);	

INSERT INTO utente VALUES ('555555', 'Danilo', 'Apicella', '86023', 'via Bibbo Baudo n.7', '1990-11-19', 123123, '2022-02-01', 234);
INSERT INTO utente VALUES ('22222', 'Raffaele', 'Della Valle', '298342', 'via lungo mare n.2', '1990-12-11', 2342323, '2022-11-12' ,232);
INSERT INTO utente VALUES ('23423', 'Fakher', 'Ferkiki', '234233', 'via albero n.3', '1990-05-04', 1242315, '2022-02-11' ,233 );
INSERT INTO utente VALUES ('23412', 'Marco', 'Antonio', '145345', 'via rimorte n.4', '1990-01-01', 123234, '2022-02-02' ,254 );
INSERT INTO utente VALUES ('12325', 'Sarah', 'Gabbani', '23423', 'via gorgoglio n.1', '2000-12-12', 213412, '2022-12-11' ,253);
INSERT INTO utente VALUES ('23456', 'Ted', 'Gasby', '45356', 'via rotonda n.4', '1998-01-01', 4686, '2022-02-02' ,254 );
INSERT INTO utente VALUES ('12321', 'Paolo', 'Russo', '245623', 'via leopardi n.1', '1967-12-12', 46584, '2022-12-11' ,253);



INSERT INTO commento VALUES ('blabla',51,'2015-12-12','555555', '23412');
INSERT INTO commento VALUES ('blabla',100,'2015-12-12','22222', '12321');
INSERT INTO commento VALUES ('Ottimo',78,'2012-11-11','23423', '12325');
INSERT INTO commento VALUES ('blabla',50,'2015-12-12','23423', '23412');



INSERT INTO ordine VALUES ('Ord01', '2002-12-12', '2017-12-12', 'sito');
INSERT INTO ordine VALUES ('Ord02', '2015-02-22', '2017-01-01', 'sito');
INSERT INTO ordine VALUES ('Ord03', '2001-01-01', '2017-02-02', 'PostaOrdinaria');
INSERT INTO ordine VALUES ('Ord04', '2005-05-05', '2017-04-02', 'Corriere');
INSERT INTO ordine VALUES ('Ord05', '2002-12-12', '2017-12-12', 'Corriere');
INSERT INTO ordine VALUES ('Ord06', '2015-02-22', '2017-01-01', 'sito');
INSERT INTO ordine VALUES ('Ord07', '2001-01-01', '2017-02-02', 'PostaOrdinaria');
INSERT INTO ordine VALUES ('Ord08', '2005-05-05', '2017-04-02', 'Corriere');


INSERT INTO software VALUES ('Sft01', 50, '555555', '12321', 'Ord01');
INSERT INTO software VALUES ('Sft02', 22, '22222', '23456', 'Ord02');
INSERT INTO software VALUES ('Sft03', 22, '23423', '12325', 'Ord06');

INSERT INTO prodotto_fisico VALUES ('Prod01', 50,'Sedia', '22222');
INSERT INTO prodotto_fisico VALUES ('Prod02', 10,'Penne',  '23423');
INSERT INTO prodotto_fisico VALUES ('Prod03', 45,'Borselli',  '555555');
INSERT INTO prodotto_fisico VALUES ('Prod04', 200,'Pianoforte' , '555555');

INSERT INTO fattura VALUES ('23412', 'Prod01', 'Ord03');
INSERT INTO fattura VALUES ('23456', 'Prod02', 'Ord04');
INSERT INTO fattura VALUES ('12321', 'Prod03', 'Ord05');
INSERT INTO fattura VALUES ('12321', 'Prod04', 'Ord07');

INSERT INTO corriere VALUES ('Bartolini', 'BartPIva', 'via Lettera fo', '3475754329');

INSERT INTO consegna VALUES ('Ord01','Fase1', '2016-11-11', 'S','Cons01',null);
INSERT INTO consegna VALUES ('Ord01', 'Fase2','2016-03-22','S', 'Cons02',null);
INSERT INTO consegna VALUES ('Ord04', '1','2016-03-22','F','Cons04',null);
INSERT INTO consegna VALUES ('Ord04', '2','2016-03-22','F', 'Cons05','BartPIva');
INSERT INTO consegna VALUES ('Ord07', '1','2016-03-22','F','Cons06',null);
INSERT INTO consegna VALUES ('Ord07', '2','2016-03-22','F', 'Cons07',null);
INSERT INTO consegna VALUES ('Ord08', '1','2016-03-22','F','Cons08',null);
INSERT INTO consegna VALUES ('Ord08', '2','2016-03-22','F', 'Cons09','BartPIva');











	