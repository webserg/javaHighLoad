FROM ubuntu:16.04

RUN apt-get update -y && \
    apt-get install -y python-pip python-dev
RUN pip install -r requirements.txt

COPY . /twitter-python.py
COPY . /requirements.txt

ENTRYPOINT ['python']
CMD ['twitter-python.py']
EXPOSE 8080