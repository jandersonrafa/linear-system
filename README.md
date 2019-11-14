# training-aws

# postgres local
docker run -p 5432:5432 --name training-aws-postgres -e POSTGRES_PASSWORD=trainingaws -e POSTGRES_USER=trainingaws -e POSTGRES_DB=trainingaws -d postgres
CREATE SCHEMA IF NOT exists AUTHORIZATION trainingaws;
CREATE TABLE trainingaws.messageconsumer (
	id bigserial NOT NULL,
	message varchar(4000) NOT NULL,
	CONSTRAINT messageconsumer_pkey PRIMARY KEY (id)
);

CREATE TABLE trainingaws.deadLetter (
	id  varchar(200)  NOT NULL,
	queueName varchar(100) NOT NULL,
	originalMessage varchar(4000) NOT NULL,
	originalHeaders varchar(4000) NOT NULL,
	resubmitMessage varchar(4000) NULL,
	resubmitHeaders varchar(4000) NULL,
	typeAction varchar(15) NOT NULL,
	CONSTRAINT deadletter_pkey PRIMARY KEY (id)
);


$ curl -X PUT http://localhost:2010/dead-letter/delete/4b663bfa-9b2e-4444-ba8b-ffa07c79c809
$ curl -X PUT http://localhost:2010/dead-letter/resubmit/4b663bfa-9b2e-4444-ba8b-ffa07c79c809?resubmitMessage=teste%20message