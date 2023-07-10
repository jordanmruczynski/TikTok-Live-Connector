const https = require("https");
const axios = require("axios");

function request(url) {
    return new Promise((resolve, reject) => {
        https.get(url, (response) => {
            let data = [];
            if (response.statusCode == 301) {
                data.push(response.headers.location);
            }
            response
                .on("data", () => {
                    resolve({
                        url: data[0],
                        headers: response.headers,
                    });
                })
                .on("error", () => {
                    reject(false);
                });
        });
    });
}

function getJson(url, config, aweme_id) {
    return new Promise((resolve, reject) => {
        axios
            .get(url, {...config})
            .then(({data}) => {
                /**
                 * Thanks to someone that asked question 10 years ago and the hero's that answers the question
                 *
                 * @link https://stackoverflow.com/questions/12462318/find-a-value-in-an-array-of-objects-in-javascript
                 **/
                let obj = data.aweme_list.find((o) => o.aweme_id === aweme_id),
                    parser = {
                        aweme_id: obj.aweme_id,
                        region: obj.region,
                        desc: obj.desc,
                        author: {
                            uid: obj.author.uid,
                            unique_id: obj.author.unique_id,
                            nickname: obj.author.nickname,
                        },
                        duration: obj.music.duration, // weird huh
                        download: {
                            nowm: obj.video.play_addr.url_list[0],
                            wm: obj.video.download_addr.url_list[0],
                            music: obj.music.play_url.url_list[0],
                            music_info: {
                                id: obj.music.id,
                                title: obj.music.title,
                                author: obj.music.author,
                                is_original: obj.music.is_original,
                                cover_hd: obj.music.cover_hd.url_list[0],
                                cover_large: obj.music.cover_large.url_list[0],
                                cover_medium: obj.music.cover_medium.url_list[0],
                            },
                        },
                    };
                /**
                 **/
                resolve(parser);
            })
            .catch(reject);
    });
}

function parse(aweme_id, share_id) {
    let config, Status, url;
    try {
        (url =
            "https" +
            "://api-h2" +
            ".tiktokv" +
            ".com/" +
            `aweme/v1/feed/?aweme_id=${aweme_id}&version_name=26.1.3&version_code=2613&build_number=26.1.3&manifest_version_code=2613&update_version_code=2613&openudid=1c556f4dd58df2c0&uuid=3454430074494840&_rticket=1666767273000&ts=${Date.now()}&device_brand=Google&device_type=Pixel%204&device_platform=android&resolution=1080*1920&dpi=420&os_version=10&os_api=29&carrier_region=US&sys_region=US%C2%AEion=US&app_name=trill&app_language=en&language=en&timezone_name=America/New_York&timezone_offset=-14400&channel=googleplay&ac=wifi&mcc_mnc=310260&is_my_cn=0&aid=1180&ssmix=a&as=a1qwert123&cp=cbfhckdckkde${share_id}&device_type=Pixel%204`),
            (config = {
                aweme_id: aweme_id,
                url: url,
                config: {
                    method: "GET",
                    headers: {
                        "user-agent":
                            "com.ss.android.ugc.trill/2613 (Linux; U; Android 10; en_US; Pixel 4; Build/QQ3A.200805.001; Cronet/58.0.2991.0",
                        "accept-encoding": "gzip",
                    },
                },
            });
        Status = true;
    } catch (_error) {
        Status = false;
    } finally {
        if (Status) return config;
        else return Status;
    }
}

async function urlParser(url) {
    return new Promise(async (resolve, reject) => {
        /**
         * It's works and I scared to recode it to better code :D
         *
         **/
        let aweme_id,
            cp,
            result,
            regex_default = /share_item_id=(.*)&share_link_id/,
            regex_1 = /\/video\/(.*)\//,
            regex_2 = /\/video\/(.*)\?_r/;
        if (/https?:\/\/(v(t|m)\.|t\.)?tiktok\.com/i.test(url)) {
            let A = await request(url);
            A.url.match(regex_1)
                ? (aweme_id = A.url.match(regex_1)[1])
                : (aweme_id = false);
            A.url.match(regex_default) ? (cp = A.url.match(regex_default)[1]) : false;
        } else {
            url.match(regex_2)
                ? (aweme_id = url.match(regex_2)[1])
                : (aweme_id = false);
            url.match(regex_default)
                ? (cp = url.match(regex_default)[1])
                : (cp = false);
        }
        result = parse(aweme_id, cp);
        result
            ? resolve(getJson(result.url, result.config, result.aweme_id))
            : reject(false);
    });
}

async function user(url) {
    return new Promise(async (resolve, reject) => {
        /**
         * Measure function time
         *
         * @link https://stackoverflow.com/questions/313893/how-to-measure-time-taken-by-a-function-to-execute
         **/
        try {
            let start = Date.now(),
                Data = await urlParser(url),
                end = Date.now(),
                processTime = end - start;
            resolve({
                process_time: processTime,
                ...Data,
            });
        } catch (e) {
            console.log(e);
            reject(e);
        }
    });
}

let url = "https://vt.tiktok.com/ZGJBtcsDq/";
user(url).then(async (json) => {
    console.log(json);
});
