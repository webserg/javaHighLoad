#!/usr/bin/env bash
for i in {1..700}
do
   curl localhost/users/$i > out.log 2> err.log
   curl localhost/visits/$i > out.log 2> err.log
   curl localhost/locations/$i > out.log 2> err.log
   curl localhost/users/$i/visits > out.log 2> err.log
   curl localhost/locations/$i/avg > out.log 2> err.log
done