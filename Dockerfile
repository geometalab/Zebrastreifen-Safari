FROM php:7-apache-jessie

MAINTAINER Nicola Jordan

# python logging in containers
ENV PYTHONUNBUFFERED=non-empty-string
ENV PYTHONIOENCODING=utf-8
ENV LC_ALL=C.UTF-8
ENV LANG=C.UTF-8
# We enable Python hash randomization to protect Django from certain DoS attacks.
# See https://docs.djangoproject.com/en/1.8/howto/deployment/checklist/#python-options
ENV PYTHONHASHSEED=random

RUN apt-get update \
  && apt-get install -y --no-install-recommends libpq-dev postgresql-client python3 python3-pip gdal-bin \
  && docker-php-ext-install pdo pdo_pgsql pgsql \
  && rm -rf /var/lib/apt/lists/*

RUN pip3 install schedule raven

RUN a2enmod rewrite

COPY updater /cronjobs

COPY web-API-php /var/www/html
