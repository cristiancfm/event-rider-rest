<template>
  <EventsList
    :title="`Upcoming <span style='font-family: Arial Black, serif'>Events</span>`"
    :get-events="getEvents"
  />
</template>

<script>
import EventsList from "@/components/events/EventsList.vue";
import EventRepository from "@/repositories/EventRepository";

export default {
  name: "EventsView",
  components: { EventsList },
  methods: {
    async getEvents(query) {
      return await EventRepository.findPublishedUpcoming(query, null);
    },
  },
};
</script>

<style scoped></style>
