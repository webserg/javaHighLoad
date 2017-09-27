#!/usr/bin/env bash
for i in {1..1000}
do
   curl localhost/users/$i/visits > out.log 2> err.log
done