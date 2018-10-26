# algamoney-api
Projeto API REST curso Algaworks Spring + Angular

#preparar app p/ deploy heroku
* Criar variáveis para parâmetros de conexão com bd
* no application-prod.properties definir as propriedades com as variáveis
* no cli do heroku criar as variáveis
* criar arquivo Procfile (raiz do projeto) e definir o comando para iniciar o app
* web: java -Dserver.port=$PORT -Dspring.profiles.active=prod $JAVA_OPTS -jar target/algamoney*.jar
* onde spring.profiles=prod indica p/ utilizar o profile prod (application-prod.properties)
* 	   -jar target/* indica o jar a ser iniciado

#heroku setup usando cli
*criar conta no heroku
*instalar heroku-cli localmente
*heroku login
*heroku create <nome do app>
*heroku addons:create jawsdb
*heroku config
*heroku config:get
*heroku config:set variavel=valor
*basear na var JAWS_DB e extrair usuario, senha e url conexão (add jdbc: tambem)

#subir alterações no heroku
*git push heroku master

#logs da app no heroku
heroku logs --tail