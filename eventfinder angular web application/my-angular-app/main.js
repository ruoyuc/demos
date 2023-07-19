
const express = require('express');
const path = require('path');
const axios = require('axios');
const cors = require('cors');
const ngeohash = require('ngeohash');

const app = express();

const angularAppPath = path.join(__dirname, 'dist/my-angular-app');
console.log(angularAppPath);
// app.use(cors());
app.use(cors({
    origin: '*'
}));
app.use(express.static(angularAppPath));
//app.use(express.static(path.join(__dirname, 'dist/my-angular-app')));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
//app.use(express.static('dist/my-angular-app'));
const API_KEY = 'uGMD0GULAjl9kQWmWtgRXBVnVa5dW1Bb';

const SpotifyWebApi = require('spotify-web-api-node');

const clientId = '3fcc8cdf298e428d84effceda9ce1c05';
const clientSecret = '006c055e859d4bddbeb5e035ac54e081';

const spotifyApi = new SpotifyWebApi({
    clientId: clientId,
    clientSecret: clientSecret
});


class ticketMaster {

    async autoComplete(text) {
        const url = `https://app.ticketmaster.com/discovery/v2/suggest.json?apikey=${API_KEY}&keyword=${text}`;
        try {
            const response = await axios.get(url);
            const data = response.data;

            if (data._embedded && data._embedded.attractions) {
                const attractions = data._embedded.attractions;
                const names = attractions.map(attraction => attraction.name);

                console.log(names);
                return names;
            } else {
                console.log('No attractions found.');
                return [];
            }
        } catch (error) {
            console.error('Error fetching data from Ticketmaster Suggest API:', error);
            return [];
        }
    }

    async search(searchrequest) {
        const keyword = searchrequest.Keyword.replace(/[\s,]+/g, "+");
        const categories = searchrequest.Category;
        const location = searchrequest.Location;
        const distance = searchrequest.Distance;
        const radius = distance;
        const loc = location.split(',');
        const latitude = parseFloat(loc[0]);
        const longitude = parseFloat(loc[1]);
        const geo = ngeohash.encode(latitude, longitude, 7);
        const params = {
            apikey: API_KEY,
            keyword: keyword,
            segmentId: categories,
            radius: radius,
            unit: 'miles',
            //latlong : `${latitude},${longitude}`,
            geoPoint: geo,
        };

        const urlTicket = 'https://app.ticketmaster.com/discovery/v2/events.json';

        try {
            const response = await axios.get(urlTicket, { params });
            const data = response.data;

            if ('_embedded' in data) {
                const events = data._embedded.events;

                const array = [];

                events.forEach(event => {
                    const local_time = event.dates.start.localTime || "";
                    const local_date = event.dates.start.localDate;
                    const images = event.images.map(image => image.url);
                    const image = images[0];
                    const name = event.name;
                    const event_id = event.id;
                    //const event_url = event.url;
                    const segment = event.classifications[0].segment.name;
                    const venue_name = event._embedded.venues[0].name;

                    const dict = {
                        'Date': local_date + ' ' + local_time,
                        'Local_date': local_date,
                        'Local_time': local_time,
                        'Icon': image,
                        'Event': name,
                        'Genre': segment,
                        'Venue': venue_name,
                        'Id': event_id,
                        //'Url': event_url,
                    };
                    array.push(dict);
                });

                array.sort((a, b) => {
                    const dateA = new Date(a.Date);
                    const dateB = new Date(b.Date);
                    return dateA - dateB;
                });

                return array;
            } else {
                return [];
            }
        } catch (error) {
            console.error(error);

            return [];
        }

    }

    async event(id) {
        // const id = req.query.id;
        const url = `https://app.ticketmaster.com/discovery/v2/events/${id}`;
        const params = { apikey: 'uGMD0GULAjl9kQWmWtgRXBVnVa5dW1Bb' };

        try {
            const response = await axios.get(url, { params });
            const event = response.data;
            const name = event.name;
            const local_date = event.dates.start.localDate || 'N/A';
            const local_time = event.dates.start.localTime || '';
            const attractions = event._embedded.attractions || [];
            const artist = attractions.map(attraction => attraction.name).join(' | ');
            const artArray = (artist.split('|')).slice(0, 10);
            const venue = event._embedded.venues[0].name || 'N/A';
            const link = attractions[0]?.url || ' ';
            const classifications = event.classifications || [];
            const genre = classifications.map(classification => `${classification.subGenre?.name || 'N/A'}|${classification.genre?.name || 'N/A'}|${classification.segment?.name || 'N/A'}|${classification.subType?.name || 'N/A'}|${classification.type?.name || 'N/A'}`).join(' / ');
            // Split the string by '|', and filter out 'Undefined' values
            const filteredValues = genre.split('|').filter(value => value !== 'Undefined' && value !== 'N/A');
            // Check if the 3rd value (at index 2) is 'Music'
            //const isMusic = filteredValues[2] === 'Music';
            let isMusic = false;
            if (filteredValues.includes('Music')) {
                isMusic = true;
              }
            const joinedString = filteredValues.join('|');

            const price_ranges = event.priceRanges || 'N/A';
            const currency = price_ranges !== 'N/A' ? event.priceRanges[0].currency : '';
            const price = price_ranges !== 'N/A' ? price_ranges.map(price_range => `${price_range.min} - ${price_range.max}`).join(' - ') : 'N/A';
            const ticket_status = event.dates.status?.code || 'N/A';
            const buy_ticket_at = event.url || 'N/A';
            const seatmap = event.seatmap?.staticUrl || 'N/A';

            const Array_event = {
                'Name': name,
                'Date': local_date + ' ' + local_time,
                'Artist': artist,
                'Venue': venue,
                'Genre': joinedString,
                'Price': price,
                'Status': ticket_status,
                'Buy': buy_ticket_at,
                'SeatMap': seatmap,
                'Link': link,
                'Currency': currency,
                'isMusic': isMusic,
                'artArray': artArray,
                'Id': id,
            };
            // res.json(Array_event);

            return Array_event;


        } catch (error) {
            console.error(error);
            return {};

        }
    }

    async getAccessToken() {
        try {
            const data = await spotifyApi.clientCredentialsGrant();
            spotifyApi.setAccessToken(data.body['access_token']);
        } catch (error) {
            console.error('Error getting access token:', error);
        }
    }


    async searchArtists(keyword) {
        try {
            const data = await spotifyApi.searchArtists(keyword);
            return data.body;
        } catch (error) {
            if (error.statusCode === 401) {
                await this.getAccessToken();
                return await this.searchArtists(keyword);
            }
            console.error('Error searching for artists:', error);
        }
    }

    async extractArtistInfo(searchResults) {
        return searchResults.artists.items.map(artist => {
            return {
                name: artist.name,
                followers: artist.followers.total,
                popularity: artist.popularity,
                spotifyLink: artist.external_urls.spotify,
                // logo: typeof(artist.images[0]),
                logo: artist.images[0]?.url,
                id: artist.id,
            };
        });
    }


    async getArtistAlbums(artistId, options) {
        try {

            const arr = [];
            const response = await spotifyApi.getArtistAlbums(artistId, options);
            const albums = response.body.items.map(album => ({
                albumName: album.name,
                imageUrl: album.images[0]?.url || 'No image available'
            }));

            for (const album of albums) {
                // console.log(album.imageUrl);
                arr.push(album.imageUrl);
            }
            // console.log('Albums:', albums);
            return arr;

        } catch (error) {
            console.error(error);

        }
    }

    async venue(vlabel) {
        // const API_KEY = 'uGMD0GULAjl9kQWmWtgRXBVnVa5dW1Bb';
        vlabel = vlabel.trim().replace(/\s+/g, "%20");
        const url = `https://app.ticketmaster.com/discovery/v2/venues.json?apikey=${API_KEY}&keyword=${vlabel}`;

        try {
            const response = await axios.get(url);
            const data = response.data;
            const venues = data._embedded?.venues || {};

            let result = {};

            if (Object.keys(venues).length !== 0) {
                const venue = venues[0];
                const picture = venue.images?.[0]?.url || '';
                const name = venue.name;
                const address = venue.address.line1;
                const city = venue.city.name;
                const state = venue.state.name;
                const longitude = venue.location.longitude;
                const latitude = venue.location.latitude;
                const phone = venue?.boxOfficeInfo?.phoneNumberDetail || 'N/A';
                const open = venue?.boxOfficeInfo?.openHoursDetail || 'N/A';
                const grule = venue?.generalInfo?.generalRule || 'N/A';
                const crule = venue?.generalInfo?.childRule || 'N/A';
                const state_code = venue.state?.stateCode || 'N/A';
                const postal_code = venue.postalCode;
                const url = venue.url;

                result = {
                    Name: name,
                    Address: address,
                    City: city + ',' + state,
                    Phone: phone,
                    Open: open,
                    Grule: grule,
                    Crule: crule,
                    //   State: state,
                    Postal: postal_code,
                    Upcoming: url,
                    Picture: picture,
                    Longitude: longitude,
                    Latitude: latitude,
                };
            }

            //   return JSON.stringify(result);
            return result
        } catch (error) {
            console.error('Error fetching venue data:', error);
            return {};
        }
    }

}



const source = new ticketMaster();



// Add your route handlers here
app.get('/searchEvent', async (req, res) => {

    const sercontent = await source.search(req.query);
    res.json(sercontent);


});

app.get('/event', async (req, res) => {
    // Implement the event route handler

    const evecontent = await source.event(req.query.id);
    res.json(evecontent);





});


app.get('/autocomplete', async (req, res) => {

    const content = await source.autoComplete(req.query.text);
    res.json(content);
});


app.get('/spotify', async (req, res) => {
    // Implement the spotify route handler
    const attractionName = req.query.name;
    const searchResults = await source.searchArtists(attractionName);
    //console.log(searchResults)
    const artistsInfo = await source.extractArtistInfo(searchResults);
    console.log(artistsInfo[0]);
    res.json(artistsInfo[0]);

});

app.get('/albums', async (req, res) => {
    const artistId = req.query.id; // Pitbull's artist ID
    const options = { limit: 10 }; // Limit the number of albums to 3
    const albums = await source.getArtistAlbums(artistId, options);
    // console.log(typeof(albums))
    res.json(albums)
});

app.get('/venue', async (req, res) => {
    // console.log('hi')
    const name = req.query.name;
    const details = await source.venue(name);
    // console.log(typeof(albums))
    res.json(details)


});


app.get('/*', (req, res) => {
    res.sendFile(path.join(angularAppPath, 'index.html'));
    //res.sendFile(path.join(__dirname, 'dist/my-angular-app/index.html'));
    //res.sendFile('dist/my-angular-app/index.html');
});

const port = process.env.PORT || 8080;
// process.env.PORT ||

app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});


