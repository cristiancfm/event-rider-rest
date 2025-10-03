<template>
  <div style="border: 1px solid var(--bs-border-color-translucent)">
    <div id="map" ref="map" :style="{ height: height + 'px' }"></div>
  </div>
</template>

<script>
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import { MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE } from "@/constants";

export default {
  name: "EventMap",
  data() {
    return {
      map: {},
      markers: [],
    };
  },
  props: {
    events: {
      type: Object,
      required: true,
    },
    latitude: {
      type: Number,
      required: false,
    },
    longitude: {
      type: Number,
      required: false,
    },
    zoom: {
      type: Number,
      required: false,
    },
    height: {
      //the height of the map in pixels
      type: Number,
      required: false,
      default: 500,
    },
    showInMapEvent: {
      type: Object,
      required: false,
    },
  },
  methods: {
    createMap() {
      // Initialize the map
      this.map = L.map(this.$refs.map);
      if (this.latitude && this.longitude && this.zoom) {
        this.map.setView([this.latitude, this.longitude], this.zoom);
      } else if (this.events.length > 0) {
        // Use first event of the list to set the view
        this.map.setView(
          [this.events[0].coordinateX, this.events[0].coordinateY],
          13
        );
      } else {
        // Set A Coruña as starting view
        this.map.setView([MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE], 13);
      }
      // Create base layer
      const osmLayer = L.tileLayer(
        "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
        {
          maxZoom: 19,
          attribution:
            '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }
      ).addTo(this.map);
      // Add layers to the map
      const baseLayers = {
        Default: osmLayer,
      };
      L.control.layers(baseLayers).addTo(this.map);
      //Set URLs for Leaflet marker icons
      delete L.Icon.Default.prototype._getIconUrl;
      L.Icon.Default.mergeOptions({
        iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
        iconUrl: require("leaflet/dist/images/marker-icon.png"),
        shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
      });
    },
    addMarkers() {
      this.events.map((e) => {
        let marker = L.marker([e.coordinateX, e.coordinateY]).addTo(this.map);
        marker.bindPopup(
          "<b><a href='/events/" +
            e.id +
            "'>" +
            e.title +
            "</a></b><br>" +
            e.locationDetails
        );
        // Add popup close event
        marker.on("popupclose", () => {
          this.$emit("popup-closed");
        });
        this.markers.push(marker);
      });
      // Set map view
      if (this.latitude && this.longitude && this.zoom) {
        this.map.setView([this.latitude, this.longitude], this.map.getZoom());
      } else if (this.events.length > 0) {
        // Use first event of the list to set the view
        this.map.setView(
          [this.events[0].coordinateX, this.events[0].coordinateY],
          this.map.getZoom()
        );
      } else {
        // Set default view
        this.map.setView(
          [MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE],
          this.map.getZoom()
        );
      }
    },
    removeMarkers() {
      this.markers.forEach((marker) => {
        marker.remove();
      });
      this.markers = [];
    },
  },
  mounted() {
    this.createMap();
    this.addMarkers();
  },
  watch: {
    events() {
      this.removeMarkers();
      this.addMarkers();
    },
    showInMapEvent() {
      if (this.showInMapEvent !== null) {
        // Look for the selected event marker
        const selectedMarker = this.markers.find((marker) => {
          return (
            marker.getLatLng().lat === this.showInMapEvent.coordinateX &&
            marker.getLatLng().lng === this.showInMapEvent.coordinateY
          );
        });
        // If found, open its popup and center map view
        if (selectedMarker) {
          selectedMarker.openPopup();
          this.map.setView(
            [selectedMarker.getLatLng().lat, selectedMarker.getLatLng().lng],
            this.map.getZoom()
          );
        }
      }
    },
  },
};
</script>

<style scoped></style>
