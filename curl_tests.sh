# automatic tests
docker-compose down
docker-compose build
docker-compose up -d
echo waiting for startup..
sleep 12

echo "------------------------"
echo "list all materials"
curl -X GET "http://localhost:8001/materials/" -H  "accept: application/json"

echo "------------------------"
echo "set titanium to 20"
curl -X PUT "http://localhost:8001/materials/Titanium?value=30" -H  "accept: application/json"
echo "------------------------"
echo "set plasma to 20"
curl -X PUT "http://localhost:8001/materials/Plasma?value=20" -H  "accept: application/json"

echo "------------------------"
echo "list all materials"
curl -X GET "http://localhost:8001/materials/" -H  "accept: application/json"

echo "------------------------"
echo "------------------------"
echo "------------------------"

echo "post a new defense"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo

echo "list defenses"
echo "------------------"
curl -X GET --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo


echo "order building a deck"
echo "------------------"
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/1?status=BuildingDeck'
echo

echo "post 1 min of time"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/time'
echo


echo "list defenses (deck should be completed)"
echo "------------------"
curl -X GET --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo

echo "order building a cannon"
echo "------------------"
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/1?status=BuildingCannon'
echo



echo "post 1 min of time"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/time'
echo


echo "list defenses (cannon still building)"
echo "------------------"
curl -X GET --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo

echo "post 1 min of time"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/time'
echo

echo "list defenses (cannon should be completed)"
echo "------------------"
curl -X GET --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo




echo "------------------------------------------------------"
echo "-------test cannon queue goes before deck queue ------"
echo "------------------------------------------------------"


echo "post a new defense (2)"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo
echo "post a new defense (3)"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'

echo "order building a deck on (2)"
echo "------------------"
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/2?status=BuildingDeck'
echo
echo "order building a deck on (3)"
echo "------------------"
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/3?status=BuildingDeck'
echo

echo "post 1 min of time"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/time'
echo


echo "list defenses (deck (2) should be completed, but not (3))"
echo "------------------"
curl -X GET --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo

echo "order building a cannon on (2)"
echo "------------------"
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/2?status=BuildingCannon'
echo


echo "post 1 min of time"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/time'
echo

echo "post 1 min of time"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/time'
echo


echo "list defenses (cannon (2) should be completed before (3) deck "
echo "------------------"
curl -X GET --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo

echo "------------------------------------------------------"
echo "-------test material costs ---------------------------"
echo "------------------------------------------------------"

echo "------------------------"
echo "list all materials (should be less after the previous tests"
curl -X GET "http://localhost:8001/materials/" -H  "accept: application/json"
echo

echo "titanium should be 3, check we get an error trying to build another deck"
echo "post a new defense (4)"
echo "------------------"
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
echo

echo "order building a deck on (4)"
echo "------------------"
curl -v -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/2?status=BuildingDeck'
echo

echo "there should be a 403 error resoponse"


docker-compose down
